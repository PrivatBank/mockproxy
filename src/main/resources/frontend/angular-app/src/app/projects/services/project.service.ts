import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private getProjectsUrl = '/project/all';
  private createProjectUrl = '/project';
  private removeProjectUrl = '/project/';
  private getUsersUrl = '/user/search?q=';

  constructor(
    private http: HttpClient
  ) { }

  getProjects(): any {
    return this.http.get<any>(this.getProjectsUrl);
  }

  getUsers(name: any): any {
    return this.http.get<any>(`${this.getUsersUrl}${name}`);
  }

  createProjects(project: any): any {
    return this.http.post<any>(this.createProjectUrl, project);
  }

  updateProject(project: any): any {
    return this.http.put<any>(this.createProjectUrl, project);
  }

  removeProjects(idProject: any): any {
    return this.http.delete<any>(this.removeProjectUrl + idProject);
  }
}
