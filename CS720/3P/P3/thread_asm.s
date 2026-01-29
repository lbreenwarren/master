.text
.globl asm_yeild
.globl asm_start
.globl asm_yeild_terminate
.extern thread_start

asm_yeild:
    pushq %rbp
    movq %rsp, %rbp
    
    #save current state of thread
    movq    %rbx, 8(%rdi)
    movq    %r12, 32(%rdi)
    movq    %r13, 40(%rdi)
    movq    %r14, 48(%rdi)
    movq    %r15, 56(%rdi)
    movq    %rsp, 0(%rdi)
    
    #restore state from incoming thread
    movq    8(%rsi), %rbx
    movq   32(%rsi), %r12
    movq   40(%rsi), %r13
    movq   48(%rsi), %r14
    movq   56(%rsi), %r15

    movq %rsi, %rdi

    movq 0(%rsi), %rsp

    popq %rbp
    ret

asm_yeild_terminate:
    pushq %rbp
    movq %rsp, %rbp

    #restore state from incoming thread
    movq    8(%rdi), %rbx
    movq   32(%rdi), %r12
    movq   40(%rdi), %r13
    movq   48(%rdi), %r14
    movq   56(%rdi), %r15

    movq 0(%rdi), %rsp

    popq %rbp
    ret

asm_start:
    pushq %rbp
    movq %rsp, %rbp

    movq 24(%rdi), %rsi
    movq 16(%rdi), %rdi
   
    call thread_start
    
    popq %rbp
    ret
