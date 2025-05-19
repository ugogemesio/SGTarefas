import { Component, Input, OnInit } from "@angular/core";
import { TaskCreateDTO, TaskResponseDTO } from "../models/task.model";
import { UserResponseDTO } from "../../users/models/user.model";
import { TaskService } from "../services/task.service";
import { UserService } from "../../users/services/user.service";
import { CommonModule } from '@angular/common';
import { FormsModule, MaxLengthValidator } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FormModalComponent } from "../../../shared/form-modal/form-modal.component";
import { ConfirmModalComponent } from "../../../shared/confirm-modal-component/confirm-modal-component";
import { switchMap } from "rxjs";

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormModalComponent, ConfirmModalComponent, FormsModule, RouterModule],
  templateUrl: './task-sec.component.html',
  styleUrl: './task-sec.component.css'
})
export class TaskSecComponent implements OnInit {
  tasks: TaskResponseDTO[] = [];
  users: UserResponseDTO[] = [];
  isEditMode = false;
  isModalOpen = false;
  taskSelecionada: TaskResponseDTO | null = null;
  loading = true;
  errorMessage: string | null = null;
  selectedStatus: string = '';
  selectedUserId: number | undefined;
  isConfirmModalOpen = false;
  taskIdToDelete: number | null = null;
  confirmationMessage = 'Deseja realmente excluir esta tarefa?';

  @Input() initialData: any;
  statusOptions = [
    { label: 'Pendente', value: 'PENDENTE' },
    { label: 'Em andamento', value: 'EM_ANDAMENTO' },
    { label: 'Concluído', value: 'CONCLUIDO' }
  ];
  formFields = [
    {
      name: 'titulo',
      label: 'Título',
      type: 'text',
      required: true,
      minLength: 3,
      maxLength: 100
    },
    {
      name: 'descricao',
      label: 'Descrição',
      type: 'text',
      required: true,
      minLength: 3,
      maxLength: 255
    },
    {
      name: 'status',
      label: 'Status',
      type: 'select',
      required: true,
      options: this.statusOptions
    },
    {
      name: 'userId',
      label: 'Usuário',
      type: 'select',
      required: true,
      options: [] // atualizado dinamicamente
    }
  ];



  constructor(
    private taskService: TaskService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.carregarDadosIniciais();
  }
  aplicarFiltros() {
    this.loading = true;

    this.taskService.filtrarTasks(
      this.selectedStatus || undefined,
      this.selectedUserId
    ).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
      },
      error: (err) => {
        this.handleError('Erro ao filtrar tarefas');
        this.loading = false;
      }
    });
  }
  private carregarDadosIniciais() {
    this.loading = true;
    this.userService.listarUsuarios().pipe(
      switchMap(users => {
        this.users = users;
        this.atualizarOpcoesUsuario();
        return this.taskService.listarTodasTasks();
      })
    ).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
      },
      error: (err) => {
        this.handleError('Erro ao carregar dados');
        this.loading = false;
      }
    });
  }
  getUserName(userId: number): string {
    const user = this.users.find(u => u.id === userId);
    return user ? user.nome : 'Usuário não encontrado';
  }

  // private loadUsers() {
  //   this.userService.listarUsuarios().subscribe({
  //     next: (users) => {
  //       this.users = users;
  //       this.atualizarOpcoesUsuario();
  //     },
  //     error: (err) => this.handleError('Erro ao carregar usuários')
  //   });
  // }

  // private loadTasks() {
  //   this.loading = true;
  //   this.taskService.listarTodasTasks().subscribe({
  //     next: (tasks) => {
  //       this.tasks = tasks;
  //       this.loading = false;
  //     },
  //     error: (err) => this.handleError('Erro ao carregar tarefas')
  //   });
  // }

  private atualizarOpcoesUsuario() {
    const userField = this.formFields.find(f => f.name === 'userId');
    if (userField) {
      userField.options = this.users.map(user => ({
        label: `${user.nome} (${user.email})`,
        value: user.id.toString()
      }));
    }
  }

  handleTaskSave(formData: TaskCreateDTO) {
    const dto: TaskCreateDTO = {
      ...formData,
      userId: Number(formData.userId)
    };

    const observable = this.isEditMode && this.taskSelecionada
      ? this.taskService.editarTask(this.taskSelecionada.id, dto)
      : this.taskService.criarTask(dto);

    observable.subscribe({
      next: () => {
        this.aplicarFiltros();
        this.closeModal();
      },
      error: (err) => console.error(`Erro ao ${this.isEditMode ? 'editar' : 'criar'} tarefa`, err)
    });
  }
  openModal() {
    this.taskSelecionada = null;
    this.isEditMode = false;
    this.isModalOpen = true;
  }

  get taskParaEdicao(): any {
    if (!this.taskSelecionada) return null;

    return {
      ...this.taskSelecionada,
      userId: this.taskSelecionada.userId
    };
  }

  abrirModalEdicao(task: TaskResponseDTO) {

    this.taskSelecionada = { ...task };
    this.isEditMode = true;
    this.isModalOpen = true;

  }
  abrirModalConfirmacao(taskId: number) {
    this.taskIdToDelete = taskId;
    this.isConfirmModalOpen = true;
  }

  onConfirmDelete() {
    if (this.taskIdToDelete !== null) {
      this.taskService.excluirTask(this.taskIdToDelete).subscribe({
        next: () => {
          this.aplicarFiltros();
          this.isConfirmModalOpen = false;
        },
        error: (err) => {
          this.handleError('Erro ao excluir tarefa');
          this.isConfirmModalOpen = false;
        }
      });
    }
  }

  onCancelDelete() {
    this.isConfirmModalOpen = false;
    this.taskIdToDelete = null;
  }

  formatarStatus(status: string): string {
    return status
      .toLowerCase()
      .split('_')
      .map(palavra => palavra.charAt(0).toUpperCase() + palavra.slice(1))
      .join(' ');
  }

  closeModal() {
    this.isModalOpen = false;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDENTE': return 'badge-atrasado';
      case 'CONCLUIDO': return 'badge-concluido';
      case 'EM_ANDAMENTO': return 'badge-pendente';
      default: return '';
    }
  }

  private handleError(message: string) {
    this.errorMessage = message;
    this.loading = false;
  }

  podeEditar(task: TaskResponseDTO): boolean {
    return task.status !== 'CONCLUIDO';
  }
}