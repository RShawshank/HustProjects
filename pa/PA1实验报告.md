# PA0实验报告

## 完成情况

按照任务书和老师给的资料，PA0完成上没有很难的问题。完成的情况如下：

- 从远程仓库clone了实验代码
- 配置完成了自己的远程仓库，并且进行了测试提交
- 修改了makefile文件，使得make run等指令可以正常运行



# PA1实验报告

## 完成情况

- 完成了简易调试器要求的所有功能。

## 要求

- 实现单步执行，打印寄存器状态，扫描内存
- 实现算术表达式求值

## 知识点

#### PA的组成

- NEMU
  - CPU
  - memory
  - 设备
  - monitor（监视+调试器）
- Nexus-AM：抽象计算机
- Nanos-lite：微型操作系统内核。日后要跑起来的一个小型OS
- Navy-apps：应用程序集

- 规定：NEMU中模拟的计算机称为客户（guest）计算机，客户机运行的程序称为客户程序。
- 启动过程：
  - 初始化monitor（将客户程序读入到客户计算机中）
    
    - 调用函数为init_monitor
    
    - 解析命令行参数
    
    - 打开日志文件
    
    - 读取镜像文件
      - 使用了`getopt(argc, argv, optstr)`函数来读取命令行参数，其中`b`表示以批处理模式运行，`a`指示`mainargs`，`l`指示日志文件位置，`d`指示`diff_so_file`的位置。由于`optstr`以`-`开头，所以可以在任意参数后指定一次镜像文件的位置，再多就在日志里报错。
      - 用一个`uint8_t`数组来模拟物理内存。约定`0x100000`为镜像起始位置。
      - 对用户的参数，复制到模拟内存的`0`位置处。
      
    - 对不同的ISA作对应的初始化工作
      
      - 调用的函数为init_isa（）
      
      - 设置物理内存的起始（和结束）地址。`mips32`和`riscv32`的物理内存起始地址为`0x80000000`，于是需要作**地址映射**（实现在`memory.c`），来把程序的物理地址转换为实际`pmem`的地址。
        - 有`paddr`和`vaddr`两种访问方式，分别代表以物理地址和虚拟地址访问内存。
      - 初始化寄存器。通过设置`CPU_state`变量`cpu`的各字段来实现。

#### RTFSC

- main.c中只是声明了外部函数`init_monitor`和`ui_mainloop`但并未包含任何头文件，那编译时编译器要到哪寻找这两个函数呢？
  - 答：只要（用`extern`）作一个引用声明，指明该函数来自外部就可以了，编译器会根据输入的文件来寻找。
  - 如果有很多源文件都要使用某些函数，那总不能每个源文件都带一堆声明吧。这就是**头文件**存在的意义。
  
- 寄存器的定义在`src/isa/riscv32/include/isa/reg.h`中，其中还提供了很多辅助函数和宏。


#### 主存实现

- `uint8_t pmem[PMEM_SIZE] PG_ALIGN = {}`
- `IOMap`：主要就是low偏移，high偏移，space空间指针和callback函数指针
- 提供的接口有宏`#define vaddr_xxx isa_vaddr_xxx`、`paddr_xxx`
  - 在`riscv32`的实现中，`isa_vaddr_xxx`都是直接调用`paddr_xxx`实现的，连参数都没有改。
- 读：`paddr_read(addr, bytenum)`
  - 调用`map_inside(IOMap, addr)`检查地址是否在low和high之间
    - 若在则进行映射，然后用映射后的地址读取一个字，再用`bytenum`位移后当掩码过滤掉不需要的字节返回
    - 否则调用`map_read(addr, bytenum, fetch_mmio_map(addr))`
- 写：`paddr_write(addr, data, bytenum)`

## 简易调试器实现

### 表达式求值

#### BNF

待实现的要求：

```
<expr> ::= <decimal-number>
  | <hexadecimal-number>    # 以"0x"开头
  | <reg_name>              # 以"$"开头
  | "(" <expr> ")"
  | <expr> "+" <expr>
  | <expr> "-" <expr>
  | <expr> "*" <expr>
  | <expr> "/" <expr>
  | <expr> "==" <expr>
  | <expr> "!=" <expr>
  | <expr> "&&" <expr>
  | "*" <expr>              # 指针解引用
```

#### 数学表达式

- 先来考虑实现简单的数学表达式求值。
- 给定的实现方法是
  1. 首先针对正则表达式作词法分析
  2. 然后对**表达式**这个单元进行递归地求值，直到表达式分解到可以进行运算的情况。（分治法）
- 注意点：
  - 需要注意`-`和`*`的二义性，即是“-”有表示负数和减法；*表示乘法和取地址。区分的方式是根据其右结合性。
  - 在处理含有多个括号的表达式时，注意将其分解成小部分，运用分治的方式进行计算，不然很容易在递归的时候出现问题。此处感谢同学对我的指点。

### 监视点

- 监视点添加了`expr`，`value`和`time_order`三个字段。
  - `expr`：监视点对应的表达式
  - `value`：监视点对应的计算值
  - `time_order`：监视点对应的触发次数
- 注意点：
  - 在对监视点进行初始化的时候，要注意时机。
  - 在针对监视点池进行操作的时候，注意取点的位置。

## RISC-V ISA补充知识

- RISC-V ISA最早是仅为学术用途，但现在也想进入工业应用。
- RISC-V ISA避免对某种特定微架构或实现技术的“过度设计”，但允许对这些环境的实现
- 支持IEEE-754，32bit和64bit，支持多核和异构的多核
- 可选变长指令，可选紧凑指令编码
- RISC-V是一个ISA家族，其基于4种基础的整数ISA(base integer ISA)，并在其上提供各种扩展。
  - 基础ISA无延迟槽，且提供变长指令的支持。对带符号整数均使用2的补码。
  - 这4种基础ISA由其整数寄存器的数量和位宽（32或64），以及地址空间大小区分。
  - 注意这四种ISA之间彼此独立，**互不兼容**。
  - `RV32I`和`RV64I`分别提供32位和64位地址空间。
    - 整数计算指令，整数load和store，控制流指令
  - `RV32E`是`RV32I` 的变种，为更小的微处理器提供支持，其只有一半数量的整数寄存器。
  - `RV128I`支持128位地址空间，且整数寄存器位宽也为128位。
- 把每种RISC-V指令集的**编码空间**(encoding space)分为**不相交**的三种：标准standard，保留reserved和定制custom。
  - 标准空间是基金会定义的
    - 标准整数乘除：M
    - 标准原子操作（单处理机内的读、修改、写）：A
    - 单精度浮点：F
    - 双精度浮点：D
    - 16位形态压缩的common指令：C
  - 保留空间是未以后的指令预留的
  - 定制顾名思义是给厂商定制指令预留的
- 扩展对于不同ISA大多是兼容的，部分功能可能由些许不同

### 术语

- `hart`："HARdware Thread"的缩写。
  - 从软件角度看，一个hart指一个可以自动取指并执行RISC-V指令的资源。

### 寄存器

- 简单粗暴的x0~x31和pc，均为32位。其中x0是硬编码为恒0。
  - 从名称也可以看出，基础整数指令集并未约定各寄存器的用途。但是实际上肯定还是有约定的。
  - x1：返回地址，x5：alternative link寄存器
  - x2：栈指针sp
  

### 指令系统

- RV32I ISA中，有4种核心指令格式：R，I，S，U，都是32位定长且在主存中必须以4字节对齐。
- 对保留空间的指令解码结果是不确定的。
- 源寄存器（rs1和rs2）与目的寄存器(rd)号在指令中的位置都是确定的。
- 除了CSR指令使用了5位立即数外，其余的立即数都是**sign-extended**（符号位扩展的），并且一般都被填在最左侧，且**所有立即数**的符号位都是指令的最高位（31），以加速位扩展电路。

## 必答题

- 我选择的ISA是`riscv32`

- riscv32有哪几种指令格式?
  - 答：参见RISC-V ISA补充知识
- LUI指令的行为是什么?
  - 答：LUI用于构建32位常数，LUI将imm放到rd的高20位，低12位填充0
- mstatus寄存器的结构是怎么样的?
  - 答：RISC-V机器模式状态寄存器(mstatus)是一个MXLEN位宽的可读写寄存器，该寄存器主要用于跟踪和控制Hart电路状态![487ef26a405340af22eb3f23ac3b3976.png](https://img-blog.csdnimg.cn/img_convert/487ef26a405340af22eb3f23ac3b3976.png)

- gcc中的`-Wall`和`-Werror`有什么作用? 为什么要使用`-Wall`和`-Werror`?
  - 答：-Wall，打开gcc的所有警告。
    -Werror，它要求gcc将所有的警告当成错误进行处理。

# PA2实验报告

## 完成情况

实现了更多的指令。

- 可以运行nexus-am/tests/cputest/tests/dummy.c
- 可以运行打字小游戏以及cputest文件夹下的所有测试

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

## 前提准备——交叉编译环境

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

## AM - 裸金属(bare-metal)运行时环境

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

## 必答题

- 请整理一条指令在NEMU中的执行过程
  1. 一条指令的执行是从`nemu/src/cpu/cpu.c`中的`exec_once()`函数开始的。这个函数的作用是先把全局译码信息`decinfo`中的取指`seq_pc`设为当前`cpu.pc`，然后调用`isa_exec()`函数完成取指和执行，最后调用`update_pc()`函数更新pc。
  2. 在`isa_exec()`函数中，首先要做的是调用`instr_fetch()`函数进行取指。根据源码借助指令的最后两位来判断指令是否生效。最后调用`idex()`函数进行译码和执行。
  3. 在riscv32中，要根据`opcode[6:2]`, `funct7[31:25]`和`funct3[14:12]`来设计译码实现。采用查表和辅助函数来实现对这些字段的译码，具体的过程是通过查阅RISCV32手册把指令放在`OpcodeEntry`中，然后把指令的某字段每个值对应指令的Entry结构组织成表，如此就可以根据当前译码的字段进行查表来进行下一步具体指令的译码函数和执行函数的实现。
  4. 根据任务书可知，nemu采用了rtl语言辅助实现指令执行。
  5. 指令执行的最后是通过观察之前是否有执行跳转指令来更新PC
- RTL函数的编译与链接
  - 尝试去掉static后，没有报错和警告，运行正常。这是因为函数没有引用内部链接的标识符，并且没有定义static相关的变量。
  - 尝试去掉inline后，报错显示该函数被定义但没有被使用。这是因为当源文件中包含了rtl.h的话，就会复制一份该函数的定义。加上函数是用static来修饰的，其链接是内部链接。
  - 尝试去掉static和inline后，报错显示有很多multiple definition错误。这是因为去掉这两个值后，包含了rtl.h的源文件都有一份外部链接的函数定义，并且被使用的外部链接的标识符在整个程序中只有一个外部定义

- 添加一行`volatile static int dummy;`
  - nemu中有34个dummy实体，这是因为存在有34个声明。
- 修改添加的代码, 为两处`dummy`变量进行初始化:`volatile static int dummy = 0;`
  - 报错显示dummy重定义。这是因为初始化后两个语句都成为了定义，但是用一个翻译单元内用一个内部链接标识符只有存在一个定义。
- make的基本认识
  - make 是一款对文件进行自动修改操作的辅助工具，当一个任务依赖一些文件（前置文件）来生成 另一些文件（目标文件）时，如果这个生成操作是可以用 shell 完成的，那么就可以使用 make 来 进一步集成和管理生成操作。

# PA3实验报告

## 完成情况

- 实现了自陷操作
- 添加了用户程序的加载和要求中的系统调用
- 完成了任务书中的测试程序和对仙剑奇侠传的运行

## 知识点

### 最简单的操作系统

- 注意Nanos-lite是运行在AM之上的，即AM的API在Nanos-lite中都是可用的。
- Nanos-lite目前的行为
  - 打印Project-N的logo, 并通过`Log()`输出hello信息和编译时间.
    - Nanos-lite和NEMU是两个独立的项目, 它们的代码不会相互影响
  - 初始化ramdisk
    - Nanos-lite把其中的一段内存作为磁盘来使用
  - 调用`init_device()`对设备进行一些初始化操作
  - `init_fs()`和`init_proc()`, 分别用于初始化文件系统和创建进程
  - 调用`panic()`结束Nanos-lite的运行
- Nanos-lite本质上也是一个AM程序

要实现一个最简单的操作系统, 就要实现以下两点功能:

- 用户程序执行结束之后, 可以跳转到操作系统的代码继续执行
- 操作系统可以加载一个新的用户程序来执行

### 等级森严的制度

- 出于对OS的保护，限制用户程序对执行流的随意切换

- riscv32则有机器模式, 监控者模式和用户模式
  - 操作系统运行在监控者模式, 用户进程运行在用户模式
  - 这些保护都是通过硬件实现的

- 保护机制需要软硬件配合，有很多细节。对于NEMU，目前暂时不需要保护机制，即所有用户进程都运行在最高特权级。
- 除此之外，执行流切换本身还要实现。OS加载用户程序好说，而用户程序切换回OS则比较麻烦，因为要切换到一个相对固定的位置。ISA使用**自陷**来实现这一操作。
  - 程序执行自陷指令后，OS会跳转到异常入口地址
  - riscv32提供`ecall`指令作为自陷指令, 并提供一个stvec寄存器来存放异常入口地址
  - 为了保存程序当前的状态, riscv32提供了一些特殊的系统寄存器, 叫控制状态寄存器(CSR寄存器). 在PA中, 我们只使用如下3个CSR寄存器:
    - sepc寄存器 - 存放触发异常的PC
    - sstatus寄存器 - 存放处理器的状态
    - scause寄存器 - 存放触发异常的原因
  - riscv32触发异常后硬件的响应过程如下:
    1. 将当前PC值保存到sepc寄存器
    2. 在scause寄存器中设置异常号
    3. 从stvec寄存器中取出异常入口地址
    4. 跳转到异常入口地址
  - **值得强调的是上述过程都是由硬件自动完成的**
- riscv32通过`sret`指令从异常处理过程中返回, 它将根据sepc寄存器恢复PC.

### 将上下文管理抽象成CTE

将上下文管理的功能划入到AM的一类新的API中, 名字叫CTE(ConText Extension).

- riscv32中通过`ecall`指令来进行自陷
- 首先当然是引发这次执行流切换的原因
  - CTE将其抽象为“事件”`_Event`，定义在`am.h`
  - 目前只需要事件编号。不同的架构只需用同一编号描述同一原因即可。
    - `cause`和`ref`是一些描述事件的补充信息, `msg`是事件信息字符串
- 然后可能要保存程序的上下文（一些寄存器），OS可能会根据这些信息进一步处理。
- 最后还有另外两个统一的API:
  - `int _cte_init(_Context* (*handler)(_Event ev, _Context *ctx))`用于进行CTE相关的初始化操作. 其中它还接受一个来自操作系统的事件处理回调函数的指针, 当发生事件时, CTE将会把事件和相关的上下文作为参数, 来调用这个回调函数, 交由操作系统进行后续处理.
  - `void _yield()`用于进行自陷操作, 会触发一个编号为`_EVENT_YIELD`事件. 不同的ISA会使用不同的自陷指令来触发自陷操作。

### 设置异常入口地址

放入CTE中实现。

- 在`nanos-lite/include/common.h`中定义宏`HAS_CTE`, 这样以后, Nanos-lite会多进行一项初始化工作: 调用`init_irq()`函数, 这最终会调用位于`nexus-am/am/src/$ISA/nemu/cte.c`中的`_cte_init()`函数
  - `_cte_init()`函数功能之一：设置异常入口地址
    - 对于riscv32来说, 直接将异常入口地址设置到stvec寄存器中即可
  - `_cte_init()`函数功能之二：注册一个事件处理回调函数，这个回调函数由Nanos-lite提供

### 触发自陷操作

测试异常入口地址是否已经设置正确。

定义了宏`HAS_CTE`后, Nanos-lite会在`panic()`前调用`_yield()`来触发自陷操作

- 需要在NEMU中实现`raise_intr()`函数(在`nemu/src/isa/$ISA/intr.c`中定义) 来模拟上文提到的异常响应机制.
- 注意由于异常响应是通用操作，所以直接在`raise_intr()`中实现即可， 而不要在指令的helper函数中实现。

#### 自陷的实现

- sepc是`0x141`，scause是`0x142`，stvec是`0x105`。

  - 上述CSR寄存器放在`CPU_state`结构里面

- 观察到后续需要使用到csrw、ecall等指令，故返回到译码和执行函数中将对应的指令实现。

- 把`__am_asm_trap`函数地址传入指令以写入异常入口地址

  ```c
   asm volatile("csrw stvec, %0" : : "r"(__am_asm_trap));
  ```

- AM中的`_yield()`函数的实现是调用`li a7, -1;`

  - 调用` ecall`后如果发现是a7(x17)=-1进入自陷

- 当NEMU发现a7=-1，接下来就调用`raise_intr()`

- 在`raise_intr()`中，就按照之前说的，完成以下动作：

  - 把当前PC保存到sepc寄存器
  - 在scause寄存器中设置异常号`_EVENT_YIELD`
  - 从stvec寄存器中取出异常入口地址
  - 跳转到异常入口地址

- 自陷操作到此就完成了。

### 保存上下文

跳转到异常入口地址后，需要用到通用寄存器。故在保存通用寄存器之前的内容时需要用到`sw`指令将各个通用寄存器依次压栈

此外，上下文还包括

- 触发异常时的PC和处理器状态
  - 对于riscv32来说, 就是epc/sepc和status/sstatus寄存器, 异常响应机制把它们保存在相应的系统寄存器中, 我们还需要将它们从系统寄存器中读出, 然后保存在堆栈上
- 异常号
  - 对于riscv32来说， 异常号已经由硬件保存在cause/scause寄存器中, 我们还需要将其保存在堆栈上.
- 地址空间
  - riscv32则是将地址空间信息与0号寄存器共用存储空间, 反正0号寄存器的值总是0, 也不需要保存和恢复. 不过目前我们暂时不使用地址空间信息

### 事件分发

- `__am_irq_handle()`函数应把异常原因打包为事件`_Event`，并调用之前初始化CTE时注册的事件处理函数`do_event()`，`do_event`再根据事件异常号进行对应的处理。
  - 在`do_event()`中识别出自陷事件`_EVENT_YIELD`, 然后输出一句话即可, 目前无需进行其它操作.
- 目前只需在`__am_irq_handle()`中根据异常号识别出`_EVENT_YIELD`事件并打包，然后`do_event()`只需输出一句话即可。

### 恢复上下文

- 首先返回到`trap.S`的`__am_asm_trap()`中, 其剩余的代码会重新把栈中存储的上下文信息恢复到寄存器中，然后调用`sret`指令取sepc返回。
  - 需要注意的是，有的异常（比如自陷）需要返回到原sepc+4的位置，而有的异常（比如缺页异常）直接返回原sepc即可，因为还需要再次尝试原指令

### BuG和问题记录

- 第一次跑的时候，发现是译码和执行函数实现的有问题。但是很快的解决了。改的很乱，又重新恢复了pa2的代码
- 在事件分发中，输出的异常号很奇怪，是一个很小的数，并不是一个正常的异常号。在观察log.txt文件后，才发现是之前恢复代码时忘记将寄存器进行编号与宏定义进行对应导致错误。

## 用户程序和系统调用

### 用户程序的加载

- 加载的过程就是把可执行文件中的代码和数据放置在正确的内存位置, 然后跳转到程序入口, 程序就开始执行了. 更具体的, 为了实现`loader()`函数, 我们需要解决以下问题:
  - 可执行文件在哪里?
    - 可执行文件位于ramdisk偏移为0处, 访问它就可以得到用户程序的第一个字节.
  - 代码和数据在可执行文件的哪个位置?
    - 由ELF提供的
  - 代码和数据有多少?
  - "正确的内存位置"在哪里?

- 用户程序的入口位于`navy-apps/libs/libc/src/platform/crt0.c`中的`_start()`函数, 它会调用用户程序的`main()`函数, 从`main()`函数返回后会调用`exit()`结束运行

- 用户程序运行在Nanos-lite上。由于运行时环境的差异, 我们不能把编译到AM上的程序放到操作系统上运行. 为此, 我们准备了一个新的子项目Navy-apps, 专门用于编译出操作系统的用户程序
- 为避免与Nanos-lite的内容冲突，约定用户程序链接到`0x83000000`附近，在`navy-apps/Makefile.compile`中的`LDFLAGS`变量中已经设置了。

- 编译Nanos-lite时会把用户程序等数据编入ramdisk，然后生成镜像文件，并包含进OS中。对于初始的默认测试`dummy`，可执行文件就位于ramdisk的开头，即偏移为0处。
- ELF文件格式了, 它除了包含程序本身的代码和静态数据之外, 还包括一些用来描述它们的信息
  - ELF文件提供了两个视角来组织一个可执行文件, 一个是面向链接过程的section视角, 这个视角提供了用于链接与重定位的信息(例如符号表); 另一个是面向执行的segment视角, 这个视角提供了用于加载可执行文件的信息. 使用`readelf`命令查看elf文件的信息。
    -  一个segment可能由0个或多个section组成, 但一个section可能不被包含于任何segment中.
  - ELF中采用program header table来管理segment，一个表项描述一个segment的所有属性。
    - 需要读取ELF中的program header table，对于`PT_LOAD`的segment，读取其offset，vaddr，filesz，memsz，把offset\~offset+filesz内的段读取到主存的vaddr\~vaddr+memsz内，并把vaddr+filesz~vaddr+memsz内清零。
  - 由于ELF在ramdisk中，框架代码提供了一些访问ramdisk的API（在`nanos-lite/src/ramdisk.c`中定义）。
- loader的功能, 把用户程序加载到正确的内存位置, 然后执行用户程序
  - `loader()`函数在`nanos-lite/src/loader.c`中定义, 其中的`pcb`参数目前暂不使用, 可以忽略
  - 因为ramdisk中目前只有一个文件, `filename`参数也可以忽略.
- 实现后, 在`init_proc()`中调用`naive_uload(NULL, NULL)`, 它会调用你实现的loader来加载第一个用户程序, 然后跳转到用户程序中执行

## OS的运行环境

os作为资源管理者管理着系统中的所有资源并为用户程序提供相应的服务。服务的方式以一种统一的接口【系统接口】来实现，用户程序也只能通过这一接口来请求服务。

## 系统调用

触发一个系统调用的具体过程是怎么样的呢?

- 用户程序通过自陷指令来触发系统调用
  - 在处理自陷的函数中添加处理系统调用的逻辑来实现

- `navy-apps/libs/libos/src/nanos.c`中定义的`_syscall_()`函数
  - 先把系统调用的参数依次放入寄存器中，然后执行自陷指令。
    - a7存放`_syscall_()`的`type`参数，a0~a2存储至多3个参数，a0存放系统调用的返回值。
  - 这里根据不同的ISA定义了不同的宏, 来对它们进行抽象. CTE会将这个自陷操作打包成一个系统调用事件`_EVENT_SYSCALL`, 并交由Nanos-lite继续处理——异常处理函数`do_event()`

- Nanos-lite收到系统调用事件之后, 就会调出系统调用处理函数`do_syscall()`进行处理【`do_event()`中，发现事件类型是`_EVENT_SYSCALL`后，调用`do_syscall()`函数处理】
  - 处理的过程首先通过宏`GPR1`从上下文`c`中获取用户进程之前设置好的系统调用参数
  - 再根据系统调用号进一步调用系统调用函数
- `dummy`程序, 它触发了一个`SYS_yield`系统调用（=1）和3个0参数进行系统调用
  - 待实现
    - 在`nexus-am/am/include/arch/riscv32-nemu.h`中实现正确的`GPR?`宏，用它们从上下文`c`中获得正确的系统调用参数寄存器。
    - 实现OS的`SYS_yield()`系统调用函数（直接调用CTE的`_yield()`然后返回0即可）
    - 设置系统调用的返回值

## OS之上的TRM

为了满足程序的基本计算能力, 需要有哪些条件:

- 机器提供基本的运算指令
  - 靠机器提供——就是pa2中实现的指令系统

- 能输出字符
- 有堆区可以动态申请内存
- 可以结束运行
  - `SYS_exit`系统调用

### 标准输出

输出是通过`SYS_write`系统调用来实现的

- 在`do_syscall()`中识别出系统调用号是`SYS_write`之后, 检查`fd`的值, 如果`fd`是`1`或`2`(分别代表`stdout`和`stderr`), 则把`buf`为首地址的`len`字节输出到串口，并返回输出的字节数。否则返回-1。
- Navy-apps提供了测试程序`hello`。要在Nanos-lite上运行，需要把`nanos-lite/Makefile`中ramdisk的`SINGLE_APP`换成`hello`程序。

### 堆区管理

堆区的使用情况是由libc来进行管理的, 但堆区的大小却需要通过系统调用向操作系统提出更改

- 调整堆区大小是通过`sbrk()`库函数来实现的，用于把用户程序的program break增长`inc`字节，其中`inc`可为负数。
  - program break, 就是用户程序的数据段(data segment)结束的位置。在进行程序链接的时候，`ld`会默认添加一个名为`_end`的符号指示数据段结束的位置。`malloc()`被首次调用时，会通过`sbrk(0)`查询program block的初始位置，之后就可以通过`sbrk()`调整program block的位置了。
  - 在Navy-apps的libc中，`sbrk()`最终会调用`_sbrk()`（在`libs/libos/src/nanos.c`中定义）。
    - 返回-1表示失败
    - 先记录program break的位置为`_end`（用`man 3 end`查询该符号的使用）
    - 被调用时，计算出新的program break
    - 调用`SYS_brk`系统调用让OS设置新的program break
    - 若调用成功，则系统调用返回0。此时更新记录的program break位置，并返回旧位置的值
    - 调用失败则返回-1

## 文件系统

当存在多个程序的时候，对于OS而言，依旧可以通过ramdisk的接口来访问不同的程序。但是当用户程序也要处理很多数据的时候就需要以文件的形式进行处理，用户程序又不知道文件处理ramdisk的哪一个位置。但是把ramdisk的读写接口直接提供给用户程序来使用是不可行的。所以需要OS向用户程序提供文件系统

- 文件的本质就是字节序列, 另外还由一些额外的属性构成.

- 实现的文件系统要求低

  - 每个文件大小固定
  - 写文件不允许超过原有文件大小
  - 文件数量固定，不能创建和删除
  - 没有目录

- 所以可以将每一个文件都固定在ramdisk中的某一个位置

- 用一张“文件记录表”来维护这些信息的脚本

  - 译Nanos-lite就会自动编译Navy-apps里面的所有程序, 并把`navy-apps/fsimg/`目录下的所有内容整合成ramdisk镜像, 同时生成这个ramdisk镜像的文件记录表`nanos-lite/src/files.h`

  - “文件记录表”是一个数组

    ```c
    typedef struct {
      char *name;         // 文件名
      size_t size;        // 文件大小
      size_t disk_offset;  // 文件在ramdisk中的偏移
    } Finfo;
    ```

  - 把`/`也认为是文件名的一部分

- 上述信息可以实现`read`和`write`文件读写操作。但是OS中还是存在些没有名字的文件，于是用文件描述符fd来统一编号。

  - 在Nanos-lite中，由于文件数量固定，所以可以直接使用文件记录表的下标作为fd。
  - 在Nanos-lite中，该偏移量直接由文件记录表维护，可以通过`lseek()`系统调用调整。

- 在`fs.c`中实现文件系统的文件操作，并实现对应的系统调用。

  - 

## 虚拟文件系统

- 所有设备都可以抽象为字节序列，与文件的本质是相似的。

- 对之前实现的文件操作API进行扩展，扩展后的API称为VFS（虚拟文件系统）

- 在Nanos-lite中, 实现VFS的关键就是`Finfo`结构体中的两个读写函数指针

  ```c
  typedef struct {
    char *name;         // 文件名
    size_t size;        // 文件大小
    size_t disk_offset;  // 文件在ramdisk中的偏移
    size_t open_offset;  // 文件被打开之后的读写指针
    ReadFn read;        // 读函数指针
    WriteFn write;      // 写函数指针
  } Finfo;
  ```

  其中`ReadFn`和`WriteFn`分别是两种函数指针，用于指向真正进行读写的函数，并返回成功读写的字节数。

- 由于Nanos-lite中特殊文件很少，因此约定函数指针为`NULL`时，表示该文件是一个普通文件，通过ramdisk的API进行读写。

### 串口抽象

- `stdout`和`stderr`都会输出到串口
  - 通过判断`fd`是否为`1`或`2`, 来决定`sys_write()`是否写入到串口
  - 没有大小约束，同时偏移量就没有意义
- 串口写入的实现在`nanos-lite/src/device.c`中。
- Nanos-lite也不打算支持`stdin`的读入, 因此在文件记录表中设置相应的报错函数即可.

### 输入设备抽象

- 对于系统来说，新的输入可以用文本来抽象，称为事件。定义以下事件，一个事件以`'\n'`结束
  - `t 1234`：返回系统启动后的时间，单位为毫秒
  - `kd RETURN`/`ku A`：按下/松开按键，按键名称全大写，使用AM中定义的按键名
- 把上述事件抽象为文件`/dev/events`，需要支持读操作，用户程序可以从中一次读出一个输入事件。
  - 需要注意的是，一次读取只能返回一个事件，而如果不优先读取键盘事件的话，由于时钟事件可以一直获取，键盘事件就没机会被读取了。

### VGA抽象

- 把显存抽象成文件. 显存本身也是一段存储空间, 它以行优先的方式存储了将要在屏幕上显示的像素
- 显存需要支持`lseek`。
- Nanos-lite和Navy-apps约定，把显存抽象为`/dev/fb`，屏幕大小抽象为`/proc/dispinfo`。
- 在`init_fs()`中对文件记录表中`/dev/fb`大小进行初始化

## 运行截图

![image-20210113222648204](D:/github/blog/myblog/source/images/PA1%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A/image-20210113222648204.png)

![image-20210113222608728](D:/github/blog/myblog/source/images/PA1%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A/image-20210113222608728.png)

![image-20210113222518705](D:/github/blog/myblog/source/images/PA1%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A/image-20210113222518705.png)

![image-20210113222408038](D:/github/blog/myblog/source/images/PA1%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A/image-20210113222408038.png)

## 必答题

- `c`指向的上下文结构究竟在哪里? 这个上下文结构又是怎么来的? 具体地, 这个上下文结构有很多成员, 每一个成员究竟在哪里赋值的?`$ISA-nemu.h`, `trap.S`, 上述讲义文字, 以及你刚刚在NEMU中实现的新指令, 这四部分内容又有什么联系?
  - `__am_irq_handle()`中的指针`c`实际指向调用时的`sp`,`_Context`定义在在 `am/include/arch/riscv32-nemu.h`中。该结构实在执行 `__am_asm_trap()`函数时保存的——该函数又是在`trap.s`中。
  - `_Context`的成员顺序是按照gpr、scause、sstatus、sepc来确定的。依据是`trap.s`代码——该代码C预处理语句与汇编代码的结合。其任务是保存上下文，调用`__am_irq_handle()`函数，接着恢复上下文最后是用`sret`指令返回。
- 从Nanos-lite调用`_yield()`开始, 到从`_yield()`返回的期间, 这一趟旅程具体经历了什么? 软(AM, Nanos-lite)硬(NEMU)件是如何相互协助来完成这趟旅程的? 
- 
  - `_yield()`函数使用了两条内联指令`li a7, -1`和 `ecall`通知NEMU进入内陷。
  - 然后nemu在执行`ecall`指令时检查a7，当其值是-1时，调用 `raise_intr()` 函数来触发异常。
    - `raise_intr()` 函数设置scause和sepc后跳转到stvec即Nanos-lite之前通过AM的CTE函数 `_cte_init()` 提供的异常处理函数 `__am_asm_trap()` 。
  - `__am_asm_trap()`函数的作用是保存上下文信息，然后跳转到CTE接口。
  - `_cte_init()` 提供异常处理函数`do_event()`进行异常处理，处理完毕后返回到`__am_irq_handle()`函数
  - `__am_irq_handle()`函数返回到`__am_asm_trap()`函数
  - 此时`__am_asm_trap()`函数在进行上下文的还原后调用`sret`指令从自陷状态中返回
  - 在执行完`sret`指令返回到sepc处——`_yield()`函数中ecall的下一条指令，最后从`_yield()`函数返回。

- 有关仙剑奇侠传的问题

  - 游戏存档的读取
    1. 在`navy-apps/apps/pal/src/global/global.c` 中的 `PAL_LoadGame() `中通过`libc`的 `fread()` 读 取游戏存档

    2. `fread（）` 调用libos中的`_read（）` 函数，`_read（）` 函数调用`_syscall_()`函数，最终通过执行`ecall`指令发起系统调用
    3. 然后nemu转到相应的异常处理函数地址。AM中的异常处理函数在保存上下文和进行事件抽象后，转到lite指定的事件处理函数`do_event()`函数
    4. `do_event()`函数根据抽象的事件类型转到`do_syscall()`函数进行系统调用的执行。

  - 更新屏幕

    - 在`navy-apps/apps/pal/src/hal/hal.c`中的`redraw()` 中通过 `NDL_DrawRect()` 和 `NDL_Render()` 更新屏幕

    - NDL在初始化后维护一个`canvas`，然后`NDL_DrawRect()`函数会把传入的像素存入到`canvas`对应的位置上。

    - `NDL_Render()` 调用`fseek（）`把偏移量定位到每一行起点的位置，然后调用`fwrite（）`输出`canvas`的一行，通过调用`fflush（）`来刷新缓冲。最后调用`putc()`向`fbsyncdev`输出`0 `进行同步，并调用`fflush()`刷新缓冲

      