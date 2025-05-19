import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-modal-component',
  imports: [CommonModule],
  templateUrl: './confirm-modal-component.html',
  styleUrl: './confirm-modal-component.css',
  standalone: true,
})

export class ConfirmModalComponent {
  @Input() isOpen = false;
  @Input() title = '';
  @Input() message = ' tem certeza?';

  @Output() onConfirm = new EventEmitter<void>();
  @Output() onCancel = new EventEmitter<void>();

  @Input() showConfirm = true;  
  @Input() showCancel = true;     
  @Input() confirmText = 'Confirmar';
  @Input() cancelText = 'Cancelar';
}
  