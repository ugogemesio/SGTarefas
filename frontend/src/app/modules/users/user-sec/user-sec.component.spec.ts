import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { UserSecComponent } from './user-sec.component';
import { UserService } from '../services/user.service';
import { TaskService } from '../../tasks/services/task.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { UserResponseDTO } from '../models/user.model';
import { UserCreateDTO } from '../models/user.model';
describe('UserSecComponent', () => {
  let component: UserSecComponent;
  let fixture: ComponentFixture<UserSecComponent>;
  let userService: jasmine.SpyObj<UserService>;
  let taskService: jasmine.SpyObj<TaskService>;

  const mockUsers: UserResponseDTO[] = [
    { id: 1, nome: 'Usuário 1', email: 'user1@test.com', dataCriacao: new Date().toString() },
    { id: 2, nome: 'Usuário 2', email: 'user2@test.com', dataCriacao: new Date().toString() }
  ];

  beforeEach(async () => {
    const userServiceSpy = jasmine.createSpyObj('UserService', [
      'listarUsuarios', 
      'criarUsuario',
      'editarUsuario',
      'excluirUsuario'
    ]);

    const taskServiceSpy = jasmine.createSpyObj('TaskService', [
      'usuarioPossuiTasks'
    ]);

    await TestBed.configureTestingModule({
      imports: [UserSecComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: UserService, useValue: userServiceSpy },
        { provide: TaskService, useValue: taskServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UserSecComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    taskService = TestBed.inject(TaskService) as jasmine.SpyObj<TaskService>;

    userService.listarUsuarios.and.returnValue(of(mockUsers));
    fixture.detectChanges();
  });

  it('Deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('Deve carregar usuarios ao iniciar', () => {
    expect(userService.listarUsuarios).toHaveBeenCalled();
    expect(component.users.length).toBe(2);
  });

  it('Deve abrir modal para novo usuario', () => {
    component.openModal();
    expect(component.isModalOpen).toBeTrue();
    expect(component.isEditMode).toBeFalse();
    expect(component.usuarioSelecionado).toBeNull();
  });

  it('Deve abrir modal para editar usuário', () => {
    const user = mockUsers[0];
    component.abrirModalEdicao(user);
    expect(component.isModalOpen).toBeTrue();
    expect(component.isEditMode).toBeTrue();
    expect(component.usuarioSelecionado).toEqual(user);
  });

  it('Deve lidar com a criação de usuário', fakeAsync(() => {
    const newUser: UserCreateDTO = { nome: 'Novo Usuário', email: 'new@test.com' };
    userService.criarUsuario.and.returnValue(of({} as any));
    
    component.handleUserSave(newUser);
    tick();
    
    expect(userService.criarUsuario).toHaveBeenCalledWith(newUser);
    expect(userService.listarUsuarios).toHaveBeenCalledTimes(2);
    expect(component.isModalOpen).toBeFalse();
  }));

  it('Deve lidar com atualização de usuário', fakeAsync(() => {
    const updatedUser: UserCreateDTO = { nome: 'Usuário Atualizado', email: 'updated@test.com' };
    userService.editarUsuario.and.returnValue(of({} as any));
    component.usuarioSelecionado = mockUsers[0];
    component.isEditMode = true;
    
    component.handleUserSave(updatedUser);
    tick();
    
    expect(userService.editarUsuario).toHaveBeenCalledWith(mockUsers[0].id, updatedUser);
    expect(userService.listarUsuarios).toHaveBeenCalledTimes(2);
    expect(component.isModalOpen).toBeFalse();
  }));

  it('Deve lidar com erro de e-mail ao salvar', fakeAsync(() => {
    const errorResponse = { error: 'Email já existe' };
    userService.criarUsuario.and.returnValue(throwError(() => errorResponse));
    
    component.handleUserSave({ nome: 'Test', email: 'existing@test.com' });
    tick();
    
    expect(component.modalComponent.formErrors['email']).toBe('Email já existe');
    expect(component.isModalOpen).toBeTrue();
  }));

  it('Deve confirmar exclusão de usuário', fakeAsync(() => {
    const userId = 1;
    taskService.usuarioPossuiTasks.and.returnValue(of(false));
    userService.excluirUsuario.and.returnValue(of({} as any));
    
    component.confirmarDeleteUser(userId);
    component.onConfirmDelete();
    tick();
    
    expect(taskService.usuarioPossuiTasks).toHaveBeenCalledWith(userId);
    expect(userService.excluirUsuario).toHaveBeenCalledWith(userId);
    expect(userService.listarUsuarios).toHaveBeenCalledTimes(2);
  }));

  it('Deve exibir aviso quando o usuário possui tarefas', fakeAsync(() => {
    const userId = 1;
    taskService.usuarioPossuiTasks.and.returnValue(of(true));
    
    component.confirmarDeleteUser(userId);
    component.onConfirmDelete();
    tick();
    
    expect(component.mensagemAviso).toContain('possui tarefas pendentes');
    expect(component.isAvisoModalOpen).toBeTrue();
  }));

  it('Deve fechar os modais', () => {
    component.closeModal();
    expect(component.isModalOpen).toBeFalse();

    component.onCancelDelete();
    expect(component.isConfirmModalOpen).toBeFalse();
  });
});