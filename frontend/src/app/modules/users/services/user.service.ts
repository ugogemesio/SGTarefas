import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/enviroment";
import { HttpClient } from "@angular/common/http";
import { UserCreateDTO, UserResponseDTO, UserUpdateDTO } from "../models/user.model";


@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API_URL = `${environment.USERS_API}/usuarios`;

  constructor(private http: HttpClient) {}

  listarUsuarios(): Observable<UserResponseDTO[]> {
    return this.http.get<UserResponseDTO[]>(this.API_URL);
  }

  buscarPorId(userId: number): Observable<UserResponseDTO> {
    return this.http.get<UserResponseDTO>(`${this.API_URL}/${userId}`);
  }

  criarUsuario(dto: UserCreateDTO): Observable<UserResponseDTO> {
    console.log(dto)
    return this.http.post<UserResponseDTO>(this.API_URL, dto);
  }

  editarUsuario(userId: number, dto: UserUpdateDTO): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>(`${this.API_URL}/${userId}`, dto);
  }

  excluirUsuario(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${userId}`);
  }
}