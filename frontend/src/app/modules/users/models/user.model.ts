export interface UserResponseDTO {
  id: number;
  nome: string;
  email: string;
  dataCriacao: string;
}

export interface UserCreateDTO {
  nome: string;
  email: string;
}

export interface UserUpdateDTO {
  nome?: string;
  email?: string;
}