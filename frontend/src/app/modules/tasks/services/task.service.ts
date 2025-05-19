import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { TaskResponseDTO, TaskCreateDTO, TaskUpdateDTO } from "../models/task.model";
import { Observable } from "rxjs";

import { environment } from "../../../../environments/enviroment";

@Injectable({ providedIn: 'root' })
export class TaskService {
  private readonly API_URL = `${environment.TASKS_API}/tarefas`;

  constructor(private http: HttpClient) { }

  listarTodasTasks(): Observable<TaskResponseDTO[]> {
    return this.http.get<TaskResponseDTO[]>(this.API_URL);
  }

  filtrarTasks(status?: string, userId?: number): Observable<TaskResponseDTO[]> {
    const params: { [key: string]: string } = {};

    if (status) params['status'] = status;
    if (userId) params['userId'] = userId.toString(); // Só envia se existir

    return this.http.get<TaskResponseDTO[]>(`${this.API_URL}/filtrar`, { params });
  }

  criarTask(dto: TaskCreateDTO): Observable<TaskResponseDTO> {
    return this.http.post<TaskResponseDTO>(this.API_URL, dto);
  }

  editarTask(taskId: number, dto: TaskUpdateDTO): Observable<TaskResponseDTO> {
    return this.http.put<TaskResponseDTO>(`${this.API_URL}/${taskId}`, dto);
  }

  excluirTask(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${taskId}`);
  }

  usuarioPossuiTasks(userId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.API_URL}/exists-by-user/${userId}`);
  }
}