export enum TaskStatus {
  PENDENTE = 'PENDENTE',
  EM_ANDAMENTO = 'EM_ANDAMENTO',
  CONCLUIDO = 'CONCLUIDO'
}

export interface TaskResponseDTO {
  id: number;
  titulo: string;
  descricao: string;
  status: TaskStatus;
  dataCriacao: string;
  userId: number;
}

export interface TaskCreateDTO {
  titulo: string;
  descricao: string;
  status: TaskStatus;
  userId: number;
}

export interface TaskUpdateDTO {
  titulo?: string;
  descricao?: string;
  status?: TaskStatus;
}