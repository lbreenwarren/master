.text
.align      8
.globl      getFP
.globl      getSP

getFP:
    mov %rbp, %rax
    ret

getSP:
    mov %rsp, %rax
    ret
