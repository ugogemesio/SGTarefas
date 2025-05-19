import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { TaskSecComponent } from './task-sec.component';
import { of, throwError } from 'rxjs';
import { TaskService } from '../services/task.service';
import { UserService } from '../../users/services/user.service';
import { TaskResponseDTO } from '../models/task.model';
import { UserResponseDTO } from '../../users/models/user.model';
import { TaskStatus } from '../models/task.model';
// Mocks
const statusPendente = TaskStatus.PENDENTE;
const statusConcluido = TaskStatus.CONCLUIDO;


const mockTasks: TaskResponseDTO[] = [
  { id: 1, titulo: 'Task 1', descricao: 'Descrição 1', status: statusPendente, userId: 1, dataCriacao: new Date().toString() },
  { id: 2, titulo: 'Task 2', descricao: 'Descrição 2', status: statusConcluido, userId: 2, dataCriacao: new Date().toString() }
];

const mockUsers: UserResponseDTO[] = [
  { id: 1, nome: 'Usuário 1', email: 'u1@email.com', dataCriacao: new Date().toString() },
  { id: 2, nome: 'Usuário 2', email: 'u2@email.com', dataCriacao: new Date().toString() }
];

describe('TaskSecComponent', () => {
  let component: TaskSecComponent;
  let fixture: ComponentFixture<TaskSecComponent>;
  let taskServiceSpy: jasmine.SpyObj<TaskService>;
  let userServiceSpy: jasmine.SpyObj<UserService>;

  beforeEach(async () => {
    const taskSpy = jasmine.createSpyObj('TaskService', ['listarTodasTasks', 'filtrarTasks', 'criarTask', 'editarTask', 'excluirTask']);
    const userSpy = jasmine.createSpyObj('UserService', ['listarUsuarios']);

    await TestBed.configureTestingModule({
      imports: [TaskSecComponent],
      providers: [
        { provide: TaskService, useValue: taskSpy },
        { provide: UserService, useValue: userSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskSecComponent);
    component = fixture.componentInstance;
    taskServiceSpy = TestBed.inject(TaskService) as jasmine.SpyObj<TaskService>;
    userServiceSpy = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
  });

  it('Deve carregar usuários e tarefas ao iniciar', fakeAsync(() => {
    userServiceSpy.listarUsuarios.and.returnValue(of(mockUsers));
    taskServiceSpy.listarTodasTasks.and.returnValue(of(mockTasks));

    fixture.detectChanges(); // ngOnInit
    tick(); // simula a finalização das chamadas assíncronas

    expect(component.users.length).toBe(2);
    expect(component.tasks.length).toBe(2);
    expect(component.loading).toBeFalse();
  }));

  it('Deve exibir mensagem de erro ao falhar no carregamento inicial', fakeAsync(() => {
    userServiceSpy.listarUsuarios.and.returnValue(throwError(() => new Error('Erro')));
    fixture.detectChanges();
    tick();

    expect(component.errorMessage).toBe('Erro ao carregar dados');
    expect(component.loading).toBeFalse();
  }));

  it('Deve aplicar filtros com sucesso', fakeAsync(() => {
    taskServiceSpy.filtrarTasks.and.returnValue(of(mockTasks));
    component.selectedStatus = 'PENDENTE';
    component.selectedUserId = 1;

    component.aplicarFiltros();
    tick();

    expect(taskServiceSpy.filtrarTasks).toHaveBeenCalledWith('PENDENTE', 1);
    expect(component.tasks.length).toBe(2);
    expect(component.loading).toBeFalse();
  }));

  it('Deve lidar com erro ao aplicar filtros', fakeAsync(() => {
    taskServiceSpy.filtrarTasks.and.returnValue(throwError(() => new Error('Erro')));

    component.selectedStatus = 'PENDENTE';
    component.selectedUserId = 1;

    component.aplicarFiltros();
    tick();

    expect(component.errorMessage).toBe('Erro ao filtrar tarefas');
    expect(component.loading).toBeFalse();
  }));

  it('Deve formatar status corretamente', () => {
    expect(component.formatarStatus('EM_ANDAMENTO')).toBe('Em Andamento');
    expect(component.formatarStatus('CONCLUIDO')).toBe('Concluido');
  });

  it('Deve retornar nome do usuário corretamente', () => {
    component.users = mockUsers;
    const nome = component.getUserName(1);
    expect(nome).toBe('Usuário 1');
  });

  it('Deve abrir e fechar modal corretamente', () => {
    component.openModal();
    expect(component.isModalOpen).toBeTrue();

    component.closeModal();
    expect(component.isModalOpen).toBeFalse();
  });

  it('Deve abrir modal de confirmação de exclusão', () => {
    component.abrirModalConfirmacao(5);
    expect(component.taskIdToDelete).toBe(5);
    expect(component.isConfirmModalOpen).toBeTrue();
  });

  it('Deve cancelar exclusão', () => {
    component.onCancelDelete();
    expect(component.taskIdToDelete).toBeNull();
    expect(component.isConfirmModalOpen).toBeFalse();
  });

  it('Deve excluir tarefa com sucesso', fakeAsync(() => {
    component.taskIdToDelete = 1;
    taskServiceSpy.excluirTask.and.returnValue(of(void 0));
    taskServiceSpy.filtrarTasks.and.returnValue(of([]));

    component.onConfirmDelete();
    tick();

    expect(component.isConfirmModalOpen).toBeFalse();
    expect(taskServiceSpy.excluirTask).toHaveBeenCalledWith(1);
  }));

  it('Deve lidar com erro ao excluir tarefa', fakeAsync(() => {
    component.taskIdToDelete = 1;
    taskServiceSpy.excluirTask.and.returnValue(throwError(() => new Error('Erro')));

    component.onConfirmDelete();
    tick();

    expect(component.isConfirmModalOpen).toBeFalse();
    expect(component.errorMessage).toBe('Erro ao excluir tarefa');
  }));

  it('Deve permitir edição apenas se status não for CONCLUIDO', () => {
    const pode = component.podeEditar({ id: 1, titulo: '', descricao: '', status: statusPendente, userId: 1, dataCriacao: new Date().toString() });
    const naoPode = component.podeEditar({ id: 1, titulo: '', descricao: '', status: statusConcluido, userId: 1, dataCriacao: new Date().toString() });

    expect(pode).toBeTrue();
    expect(naoPode).toBeFalse();
  });
});
