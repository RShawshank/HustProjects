# PA2实验报告

## 进度



## 要求

- 在NEMU中运行第一个C程序`dummy`
-  实现更多的指令, 在NEMU中运行所有`cputest`
- 运行打字小游戏

## 知识点

### 指令的执行

`cpu-exec.c`没有与执行指令相关的信息，主要看`exec_once()`。

- 该文件中定义了`DecodeInfo`——结构变量`decinfo`可以被多个其他文件共享
- 设置`decinfo.seq_pc = cpu.pc`
  - `seq`代表顺序(sequential)的意思

#### 取指

`isa_exec()`：取指令、用于屏蔽ISA差异的API。

- `decinfo.seq_pc`的地址将被作为参数

`instr_fetch()`：负责取指令的工作——对内存的访问。

- 取指存到`decinfo.isa.instr.val`中

#### 译码

根据指令的`opcode`来决定——`opcode_table`数组进行索引（该数组待完成）

- `riscv32`宽度还需记录到`decinfo`
- `opcode`作为参数进入到`idex（）`函数——进行译码和执行的
- `idex（）`取得的内容——调用相应的`译码辅助函数(decode helper function)`。
  - 译码辅助函数统一通过宏`make_DHelper`(在`nemu/include/cpu/decode.h`中定义)
  - 每种译码辅助函数负责一种类型的操作数译码——对应指令中的操作数将存储在全局译码信息`decinfo`的`src`成员（源操作数）, `src2`（源操作数）成员和`dest`（目的操作数）成员中

#### 执行

- 执行辅助函数(execution helper function)来进行真正的执行操作
  - 统一通过宏`make_EHelper`(在`nemu/include/cpu/exec.h`中定义)
  - 通过RTL指令来描述指令真正的执行功能
- 注意点：riscv32访问内存只能经由特定的访存指令完成

#### 更新PC

- 为了更新PC，需要确定指令的长度——riscv32就简单多了，定长

- 首先，设置`decinfo.seq_pc = cpu.pc`

- 然后调用`isa_exec(&decinfo.seq_pc)`（`src/isa/riscv32/exec/exec.c`）

  - 其中调用`instr_fetch(seq_pc, 4)`取指存到`decinfo.isa.instr.val`中

    - 取指时使用`vaddr_read`函数。自动把`seq_pc`加上取指长度

  - 查表调用`idex(seq_pc, &opcode_table[decinfo.isa.instr.opcode6_2])`来对指令进行译码和执行操作

    

- 当代码从`isa_exec()`返回时, `decinfo.seq_pc`将会指向下一条静态指令的地址, 此时通过`update_pc()`(在`nemu/include/cpu/exec.h`中定义)对PC进行更新即可

  - `decinfo.seq_pc`在`isa_exec()`执行的过程中会随着取出的指令长度而增长。

  - 若`decinfo.is_jmp==true`则`cpu.pc = decinfo.jmp_pc`

    否则直接将其置为0

#### RTL表示指令行为

- RTL寄存器统一使用`rtlreg_t`来定义
- RTL寄存器类型：
  - 不同ISA的通用寄存器(在`nemu/include/cpu/decode.h`中定义)
  - 临时寄存器(在`nemu/src/cpu/cpu.c`中定义)

- RTL基本指令(在`nemu/include/rtl/rtl.h`中定义)
  - 无需使用临时寄存器
  - 立即数读入`rtl_li`
  - 寄存器传输`rtl_mv`
  - 32位寄存器-寄存器类型的算术/逻辑运算, 包括`rtl_(add|sub|and|or|xor|shl|shr|sar|i?mul_[lo|hi]|i?div_[q|r])`, 这些运算的定义用到了`nemu/include/rtl/c_op.h`中的C语言运算
  - 被除数为64位的除法运算`rtl_i?div64_[q|r]`
  - guest内存访问`rtl_lm`和`rtl_sm`
  - host内存访问`rtl_host_lm`和`rtl_host_sm`
  - 关系运算`rtl_setrelop`, 具体可参考`nemu/src/cpu/relop.c`
  - 跳转, 包括直接跳转`rtl_j`, 间接跳转`rtl_jr`和条件跳转`rtl_jrelop`
  - 终止程序`rtl_exit`(在`nemu/src/monitor/cpu-exec.c`中定义)
- 伪指令
  - 借助基本指令或已经实现的伪指令实现
  - ISA无关(`include/rtl/rtl.h`)
    - 32位寄存器-立即数类型的算术/逻辑运算
    - 其他常用功能
  - ISA相关（`riscv32/include/isa/rtl.h`）
    - 通用寄存器访问`rtl_lr`和`rtl_sr`
    - 其他ISA相关功能
  - ==调用约定==由于某些RTL伪指令需要使用临时寄存器存放中间结果才能实现完整功能，进行以下调用约定：
    - 尽可能不使用`dest`以外的寄存器存放中间结果
    - 实在需要使用临时寄存器时，按照以下约定使用：
      - `ir`只能作为`rtl_li`的目的寄存器
      - `t0` `t1`只能在伪指令的实现过程中存放中间结果
      - `s0` `s1`只能在译码和执行辅助函数的实现过程中存放中间结果

#### 实现新指令

1. 在`opcode_table`中填写正确的译码辅助函数, 执行辅助函数以及操作数宽度
2. 用RTL实现正确的执行辅助函数, 需要注意使用RTL伪指令时要遵守上文提到的小型调用约定

## 准备交叉编译环境

1. 下载[最新版本的xPack GNU RISC-V Embedded GCC](https://github.com/xpack-dev-tools/riscv-none-embed-gcc-xpack/releases)
2. 按照[教程](https://xpack.github.io/riscv-none-embed-gcc/install/)安装
3. 注意点：在配置环境变量的时候要通过修改.bashrc文件的方式进行配置。

#### 译码函数的实现

——make_DHelper（）函数

- 对opcode_table进行补充需要先完成对应的译码函数和执行函数。
  - OpcodeEntry的结构体中知道
- 在完成译码函数时，需要把译码信息保存到全局译码信息decinfo里。
- 查阅手册依次实现不同指令的译码函数

#### 执行函数的实现

——make_EHelper（）函数

#### opcode_table的补充

根据阅读官方文档进行补充

- 注意load指令有byte、halfword、word以及无符号数的byte、halfword的区分
- 注意store指令只有byte、halfword、word的区分
- 同理完成其他指令，例如立即数指令等

## bug

在编写跳转指令时。需要注意的是decinfo.seq_pc在执行下一条指令时会由硬件自动+4。

### AM - 裸金属(bare-metal)运行时环境

```
AM = TRM + IOE + CTE + VME + MPE
```

- TRM(Turing Machine) - 图灵机, 最简单的运行时环境, 为程序提供基本的计算能力
- IOE(I/O Extension) - 输入输出扩展, 为程序提供输出输入的能力
- CTE(Context Extension) - 上下文扩展, 为程序提供上下文管理的能力
- VME(Virtual Memory Extension) - 虚存扩展, 为程序提供虚存管理的能力
- MPE(Multi-Processor Extension) - 多处理器扩展, 为程序提供多处理器通信的能力 (MPE超出了ICS课程的范围, 在PA中不会涉及)

整个AM项目分为三大部分:

- `nexus-am/am/` - 不同架构的AM API实现, 目前我们只需要关注`nexus-am/am/src/$ISA/nemu/`以及相应的头文件即可
- `nexus-am/tests/`和`nexus-am/apps/` - 一些功能测试和直接运行在AM上的应用程序
- `nexus-am/libs/` - 一些架构无关的函数库, 方便应用程序的开发

- 通过以上模块的组合，AM可以为程序提供与架构解耦的运行时环境。也就是说，我们只需要在编写C程序时调用AM提供的库函数即可完成程序编写，在编译时使用AM提供的库进行链接即可。
- 首先需要实现更多的指令，以通过`nexus-am/tests/cputest/`下除`string`和`hello-str`外的其他测试用例。
- 然后需要实现`lib`中的一些字符串处理函数来通过剩余的测试用例
  - `strcmp`, `strcat`, `strcpy`, `memcmp`, `memset`
  - 这些函数貌似都不管`NULL`，直接让他崩掉。
  - 对于`strcmp`，更长或者同位置字符更大的字符串是较大者。

## 调试运行

[differential testing](https://en.wikipedia.org/wiki/Differential_testing)(后续简称DiffTest). 通常来说, 进行DiffTest需要提供一个和DUT(Design Under Test, 测试对象) 功能相同但实现方式不同的REF(Reference, 参考实现), 然后让它们接受相同的输入, 观测它们的行为是否相同.

## 输入输出

### IO编址方式

CPU访问设备寄存器的方式——可以将设备寄存器当作普通的寄存器将其编号进行统一的访问操作；或者将设备寄存器单独编号，采用单独的IO指令进行访问。

#### 端口IO

一种I/O编址方式是端口映射I/O(port-mapped I/O), CPU使用专门的I/O指令对设备进行访问, 并把设备的地址称作端口号. 有了端口号以后, 在I/O指令中给出端口号, 就知道要访问哪一个设备寄存器了

#### 內存映射I/O

端口映射I/O把端口号作为I/O指令的一部分, 这种方法很简单, 但同时也是它最大的缺点. 指令集为了兼容已经开发的程序, 是只能添加但不能修改的. 这意味着, 端口映射I/O所能访问的I/O地址空间的大小, 在设计I/O指令的那一刻就已经决定下来了. 所谓I/O地址空间, 其实就是所有能访问的设备的地址的集合. 随着设备越来越多, 功能也越来越复杂, I/O地址空间有限的端口映射I/O已经逐渐不能满足需求了

- 将一部分物理内存"重定向"到I/O地址空间中, CPU尝试访问这部分物理内存的时候, 实际上最终是访问了相应的I/O设备
  - CPU就可以通过普通的访存指令来访问设备
- 内存映射I/O唯一的缺点就是, CPU无法通过正常渠道直接访问那些被映射到I/O地址空间的物理内存了
- RISC架构只提供内存映射I/O的编址方式

### 映射和IO方式

架代码为映射定义了一个结构体类型`IOMap`(在`nemu/include/device/map.h`中定义), 包括名字, 映射的起始地址和结束地址, 映射的目标空间, 以及一个回调函数. 然后在`nemu/src/device/io/map.c`实现了映射的管理, 包括I/O空间的分配及其映射, 还有映射的访问接口.

- `map_read()`和`map_write()`用于将地址`addr`映射到`map`所指示的目标空间, 并进行访问
  - 访问时, 可能会触发相应的回调函数, 对设备和目标空间的状态进行更新
- `nemu/src/device/io/port-io.c`是对端口映射I/O的模拟
- `add_pio_map()`函数用于为设备的初始化注册一个端口映射I/O的映射关系. 
  - `pio_read_[l|w|b]()`和`pio_write_[l|w|b]()`是面向CPU的端口I/O读写接口, 它们最终会调用`map_read()`和`map_write()`, 对通过`add_pio_map()`注册的I/O空间进行访问

### 设备

使用SDL库来实现设备的模拟, `nemu/src/device/device.c`含有和SDL库相关的代码. `init_device()`函数首先对以上四个设备进行初始化

- 初始化设备后，会注册一个100Hz的定时器，每隔0.01秒调用一次`device_update`函数检查按键输入。该定时器是虚拟定时器，仅在NEMU处于用户态时计时；如果NEMU进行大量输出，则定时器的计时会变慢。

- 要在NEMU中启用设备，只需在`nemu/include/common.h`中定义宏`HAS_IOE`。

- 虽然设备是NEMU实现的，其已经独立于guest的架构，但是对于native程序来说仍然无法通用，因此把设备操作封装在AM的IOE中。IOE提供三个API：

  ```c
  size_t _io_read(uint32_t dev, uintptr_t reg, void *buf, size_t size);
  size_t _io_write(uint32_t dev, uintptr_t reg, void *buf, size_t size);
  int _ioe_init();
  ```

  - 其中`reg`并不是固定的地址，而是抽象的功能号。作用是为了统一NEMU和native的设备访问接口（在`nemu-common`中实现）。
  - `nexus-am/am/amdev.h`中定义了常见设备的ID和抽象功能编号。每个架构在实现IOE时都要遵循这些约定。
  - 由于经过了AM的抽象，可以使用IOE来实现一些输入输出的库函数（在`klib/src/io.c`中定义）。

#### 串口

- `nemu/src/device/serial.c`模拟了串口的功能. 

- `nemu-common/trm.c`中已经实现了AM-NEMU的串口API。
  - NEMU的串口是始终就绪的，这点与QEMU不同，所以也进行了处理。

#### 时钟

`nexus-am/am/amdev.h`中为时钟定义了两个抽象寄存器:

- `_DEVREG_TIMER_UPTIME`, AM系统启动时间. 从中读出`_DEV_TIMER_UPTIME_t`结构体, `(hi << 32LL) | lo`是系统启动的毫秒数.
- `_DEVREG_TIMER_DATE`, AM实时时钟(RTC). 从中读出`_DEV_TIMER_DATE_t`结构体, 包含年月日时分秒. PA中暂不使用.

`nemu/src/device/timer.c`模拟了i8253计时器的功能. 

- i8253计时器初始化时会分别注册`0x48`处长度为4个字节的端口, 以及`0xa1000048`处长度为4字节的MMIO空间, 它们都会映射到RTC寄存器. CPU可以访问这一寄存器, 获得当前时间(单位是ms)

#### 键盘

`nemu/src/device/keyboard.c`模拟了i8042通用设备接口芯片的功能

- i8042芯片初始化时会分别注册`0x60`处长度为4个字节的端口, 以及`0xa1000060`处长度为4字节的MMIO空间。
  - 每当用户敲下/释放按键时, 将会把相应的键盘码放入数据寄存器, CPU可以访问数据寄存器, 获得键盘码; 
  - 当无按键可获取时, 将会返回`_KEY_NONE`. 在AM中, 我们约定通码的值为`断码 | 0x8000`
- 在`nemu/src/device/keyboard.c`中维护了一个`keymap[256]`数组以及一个循环队列`key_queue`和两个指针`key_f`, `key_r`。
  - 通过`keymap[256]`数组的designator和一些宏实现了取到自定义键码的功能
  - `key_queue`队列保存的是由SDL键码通过`send_key()`函数转化成AM的键码
- `i8042_data_io_handler()`函数是键盘IO映射的回调函数。其中用`key_f`取`key_queue`的队首置入设备寄存器。若`key_f == key_r`说明队列为空，把`_KEY_NONE`置入设备寄存器。

`nexus-am/am/amdev.h`中为键盘定义了一个抽象寄存器:

- `_DEVREG_INPUT_KBD`, AM键盘控制器. 从中读出`_DEV_INPUT_KBD_t`结构体, `keydown = 1`为按下按键, `keydown = 0`为释放按键. `keycode`为按键的断码, 没有按键时, `keycode`为`_KEY_NONE`.

#### VGA

初始化VGA时还会进行一些和SDL相关的初始化工作, 包括创建窗口, 设置显示模式等. 

`nemu/src/device/vga.c`模拟了VGA的功能. VGA初始化时注册了从`0xa0000000`开始的一段用于映射到video memory的MMIO空间。

VGA初始化时注册了从`0xa0000000`开始的一段用于映射到video memory的MMIO空间. 代码只模拟了`400x300x32`的图形模式, 一个像素占32个bit的存储空间, R(red), G(green), B(blue), A(alpha)各占8 bit, 其中VGA不使用alpha的信息。

- 调色板是一个颜色信息的数组, 每一个元素占4个字节, 分别代表R(red), G(green), B(blue), A(alpha)的值
- 除此以外还有**屏幕大小寄存器**（`screensize_port_base`）和**同步寄存器**，前者NEMU已经实现，但AM未使用；后者AM已使用，但NEMU只定义而未实现。

`nexus-am/am/amdev.h`中为VGA定义了两个抽象寄存器:

- `_DEVREG_VIDEO_INFO`, AM显示控制器信息. 从中读出`_DEV_VIDEO_INFO_t`结构体, 其中`width`为屏幕宽度, `height`为屏幕高度. 另外假设AM运行过程中, 屏幕大小不会发生变化。
- `_DEVREG_VIDEO_FBCTL`, AM帧缓冲控制器. 向其写入`_DEV_VIDEO_FBCTL_t`结构体, 向屏幕`(x, y)`坐标处绘制`w*h`的矩形图像. 图像像素按行优先方式存储在`pixels`中, 每个像素用32位整数以`00RRGGBB`的方式描述颜色。