.text
.globl global_ptrs
.globl stack_ptrs
.globl reg_ptrs

global_ptrs:
    leaq __data_start(%rip), %rax
    movq %rax, (%rdi)

    leaq _end(%rip), %rax
    movq %rax, (%rsi)
    ret

stack_ptrs:

    movq (%rbp), %rax        # caller's RBP
    movq %rax, (%rsi)
    leaq 16(%rbp), %rax      # Caller's RSP
    movq %rax, (%rdi)

    ret

reg_ptrs:
    movq    %rdi, %rax     # return caller's RDI
    movq    %rsi, %rdx     # return caller's RSI
    movq    %rbx, %rcx     # return caller's RBX
    ret
