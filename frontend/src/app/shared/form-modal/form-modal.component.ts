import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-form-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './form-modal.component.html',
  styleUrls: ['./form-modal.component.css'],
  exportAs: 'formModal'
})
export class FormModalComponent {
  @Input() title: string = 'Novo Registro';
  @Input() formFields: any[] = [];
  @Input() isOpen: boolean = false;
  @Output() onClose = new EventEmitter<void>();
  @Output() onSave = new EventEmitter<any>();
  @Input() initialData: any;
  
  formData: any = {};
  formErrors: { [key: string]: string } = {};

  constructor(public cd: ChangeDetectorRef) {} // Sintaxe correta do construtor

  closeModal() {
    this.onClose.emit();
  }

  public updateErrors(errors: { [key: string]: string }) {
    this.formErrors = { ...errors };
    this.cd.detectChanges(); // Força atualização da view
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['initialData']) {
      this.formData = this.initialData ? {
        ...this.initialData,
        userId: this.initialData.userId?.toString()
      } : {};
      this.formErrors = {};
    }
  }

  private validateForm(): boolean {
    this.formErrors = {};
    let isValid = true;

    for (const field of this.formFields) {
      const value = this.formData[field.name] || '';

      if (field.required && !value.trim()) {
        this.formErrors[field.name] = 'Este campo é obrigatório';
        isValid = false;
        continue;
      }

      if (field.type === 'email' && value) {
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test(value)) {
          this.formErrors[field.name] = 'Email inválido';
          isValid = false;
        }
      }

      if (field.minLength && value.length < field.minLength) {
        this.formErrors[field.name] = `Mínimo de ${field.minLength} caracteres`;
        isValid = false;
      }
    }

    return isValid;
  }

  submitForm() {
    if (this.validateForm()) {
      this.onSave.emit(this.formData);
      this.closeModal();
    }
  }
}