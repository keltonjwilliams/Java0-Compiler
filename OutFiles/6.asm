	sys_exit	equ	1
	sys_read	equ	3
	sys_write   equ	4
	stdin		equ	0
	stdout		equ	1
	stdout		equ	1
	stderr		equ	3

section .data
	M	dw	7
	userMsg	db	'Enter an integer (less than 32,765): '
	lenUserMsg	equ	$-userMsg
	displayMsg	db	'test'
	lenDisplayMsg	equ	$-displayMsg
	Result	db	'Ans = '
	ResultValue	db	'aaaaa'
	ResultEnd	equ	$-ResultValue
	newline	db	0xA


section .bss
	a	resw	1
	b	resw	1
	c	resw	1
	d	resw	1

section .text
	global	_start
_start:
	mov ds, ax
	mov ax,[a]
	cmp ax,[M]
	JNE L1

	mov ax,[b]
	cmp ax,[M]
	JNE L1

	mov ax,[d]
	cmp ax,[M]
	JNE L1

	mov ax,[d]
	cmp ax,[M]
	JNE L1

	mov ax, [M]
	call convertIntegerToString
	mov bx,OFFSET ResultValue
	call PrintString

	mov eax,sys_exit
	xor ebx,ebx
	int 80h

ConvertIntegerToString:
	mov ebx, ResultValue + 4   ;Store the integer as a five
					;digit char string at Result for printing

ConvertLoop:
	sub dx,dx  ; repeatedly divide dx:ax by 10 to obtain last digit of number
	mov cx,10  ; as the remainder in the DX register.  Quotient in AX.
	div cx
	add dl,'0' ; Add '0' to dl to convert from binary to character.
	mov [ebx], dl
	dec ebx
	cmp ebx,ResultValue
	jge ConvertLoop

	ret

GetAnInteger:	;Get an integer as a string
	;get response
	mov eax,3	;read
	mov ebx,2	;device
	mov ecx,num	;buffer address
	mov edx,6	;max characters
	int 0x80
	;print number
	mov edx,eax 	; eax contains the number of character read including <lf>
	mov eax, 4
	mov ebx, 1
	mov ecx, num
	int 80

ConvertStringToInteger:
	mov ax,0	;hold integer
	mov [ReadInt],ax ;initialize 16 bit number to zero
	mov ecx,num	;pt - 1st or next digit of number as a string 
	mov bx,0
	mov bl, byte [ecx] ;contains first or next digit
Next:	sub bl,'0'	;convert character to number
	mov ax,[ReadInt]
	mov dx,10
	mul dx		;eax = eax * 10
	add ax,bx
	mov [ReadInt], ax

	mov bx,0
	add ecx,1 	;pt = pt + 1
	mov bl, byte[ecx]
	cmp bl,0xA	;is it a <lf>
	jne Next	; get next digit 
	ret

PrintString:
	push	ax
	push	dx
; subpgm:
	mov	eax, 4
	mov	ebx, 1
	mov	ecx, userMsg
	mov	edx, lenUserMsg
	int	80h
	pop	dx
	pop	ax
	ret

	
