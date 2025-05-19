// user-sec.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../services/user.service';
import { UserResponseDTO, UserCreateDTO } from '../models/user.model';
import { FormModalComponent } from '../../../shared/form-modal/form-modal.component';
import { Input } from '@angular/core';
import { ConfirmModalComponent } from '../../../shared/confirm-modal-component/confirm-modal-component';
import { TaskService } from '../../tasks/services/task.service';

@Component({
  selector: 'app-user-sec',
  standalone: true,
  imports: [CommonModule, FormModalComponent, ConfirmModalComponent],
  templateUrl: './user-sec.component.html',
  styleUrls: ['./user-sec.component.css']
})


export class UserSecComponent implements OnInit {
  @ViewChild(FormModalComponent) modalComponent!: FormModalComponent;
  users: UserResponseDTO[] = [];
  usuarioSelecionado: UserResponseDTO | null = null;
  isEditMode = false;
  isModalOpen = false;
  mensagemAviso: string = '';
  isAvisoModalOpen: boolean = false;

  formFields = [
    { name: 'nome', label: 'Nome', type: 'text', required: true },
    { name: 'email', label: 'Email', type: 'email', required: true }
  ];
  constructor(
    private userService: UserService,
    private taskService: TaskService
  ) { }
  ngOnInit() {
    this.loadUsers();
  }

  @Input() initialData: any; 
  handleUserSave(formData: UserCreateDTO) {
    const dto: UserCreateDTO = {
      nome: formData.nome,
      email: formData.email
    };

    const handleError = (err: any) => {

      let errorMessage = 'Erro desconhecido';

      if (typeof err.error === 'string') {

        errorMessage = err.error;
      } else if (err.error?.message) {

        errorMessage = err.error.message;
      } else if (err.message) {

        errorMessage = err.message;
      }


      if (errorMessage.toLowerCase().includes('email')) {
        this.modalComponent.formErrors = { ...this.modalComponent.formErrors, email: errorMessage };
        this.modalComponent.cd.detectChanges();
      } else {
        this.mensagemAviso = errorMessage;
        this.isAvisoModalOpen = true;
      }
      this.isModalOpen = true;

      console.error('Erro na operação:', errorMessage);
    };

    if (this.isEditMode && this.usuarioSelecionado) {
      this.userService.editarUsuario(this.usuarioSelecionado.id, dto).subscribe({
        next: () => {
          this.loadUsers();
          this.closeModal();
        },
        error: handleError
      });
    } else {
      this.userService.criarUsuario(dto).subscribe({
        next: () => {
          this.loadUsers();
          this.closeModal();
        },
        error: handleError
      });
    }
  }

  openModal() {
    this.usuarioSelecionado = null;
    this.isEditMode = false;
    this.isModalOpen = true;
  }

  isConfirmModalOpen = false;
  userIdToDelete: number | null = null;

  confirmarDeleteUser(userId: number) {
    this.userIdToDelete = userId;
    this.isConfirmModalOpen = true;
  }
  abrirModalEdicao(user: UserResponseDTO) {
    this.usuarioSelecionado = { ...user };
    this.isEditMode = true;
    this.isModalOpen = true;
  }
  onConfirmDelete() {
    if (this.userIdToDelete !== null) {
      this.taskService.usuarioPossuiTasks(this.userIdToDelete).subscribe({
        next: (possuiTarefas) => {
          if (possuiTarefas) {
            this.mensagemAviso = 'Este usuário possui tarefas pendentes e não pode ser excluído.';
            this.isAvisoModalOpen = true;
            this.isConfirmModalOpen = false;
            this.userIdToDelete = null;
          }
          else {
            this.userService.excluirUsuario(this.userIdToDelete!).subscribe({
              next: () => {
                this.loadUsers();
                this.isConfirmModalOpen = false;
                this.userIdToDelete = null;
              },
              error: err => {
                console.error('Erro ao excluir usuário', err);
                this.isConfirmModalOpen = false;
                this.userIdToDelete = null;
              }
            });
          }
        },
        error: err => {
          console.error('Erro ao verificar tarefas do usuário', err);
          this.isConfirmModalOpen = false;
          this.userIdToDelete = null;
        }
      });
    }
  }

  onCancelDelete() {
    this.isConfirmModalOpen = false;
    this.userIdToDelete = null;
  }
  closeModal() {
    this.isModalOpen = false;
  }
  private loadUsers() {
    this.userService.listarUsuarios().subscribe(users => {
      this.users = users;
    });
  }

}