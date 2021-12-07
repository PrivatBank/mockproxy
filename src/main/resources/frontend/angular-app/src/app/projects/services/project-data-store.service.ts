import { Injectable } from '@angular/core';

import {BehaviorSubject, Observable, of, Subject} from 'rxjs';
import {ProjectService} from './project.service';
import {Router} from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class ProjectDataStoreService {
   public projectsMock: any[] = [];
  // public projectsMock: any[] = [
  //   { description: 'sefwfewerfre',
  //     id: '609e79623408bf4d5c0e7fb4',
  //     name: 'rrrrrr',
  //     users: [
  //       {login: 'cs251083gan', name: 'Гулич Олександр Миколайович', photoUrl: 'https://staff.privatbank.ua/photo/cs251083gan'},
  //       {login: 'dn020788mdj1', name: 'Малий Дмитро Юрійович', photoUrl: 'https://staff.privatbank.ua/photo/dn020788mdj1'}
  //     ],
  //     state: 'active'},
  //   {id: '607eea74554d6a1c73970681', name: 'test1234234234', description: 'wefwefweferfwefwefwferfwerfwefgwerg'},
  //   {id: '607eea74554d6a1c73970681', name: 'test344r34r', description: 'wefwefwefeerwgveyjhyjtujliolonyurfwefgwerg'}
  // ];

  private _PROJECTS = new BehaviorSubject<any[]>([]);
  private selectedProject = new BehaviorSubject<any>(null);

  constructor(
    private router: Router,
    public projectService: ProjectService
  ) {
    this.loadInitialData();
  }

  getSelectedProject(): Observable<any> {
      return this.selectedProject.asObservable();
  }

  setSelectedProject(project: any): void {
    return this.selectedProject.next(project);
  }


  get projects(): Observable<any[]> {
     return this._PROJECTS.asObservable();
  }

  getProjectsList(): any[] {
    return this._PROJECTS.getValue();
  }

  loadInitialData(): void {
    if (this.projectsMock.length > 0) {
      this._PROJECTS.next(this.projectsMock);
    }
    this.projectService.getProjects()
      .subscribe(
        (res: any[]) => {
          this.projectsMock = res;
          console.log('__this.projectsMock__', this.projectsMock);
          this._PROJECTS.next(this.projectsMock);
        },
        (err: any) => console.log('Error retrieving Projects')
      );
  }

  addProject(newProject: any): Observable <any> {

    const obs: Observable <any>  = this.projectService.createProjects(newProject);

    obs.subscribe(
      res => {
        const projects: any[] = this._PROJECTS.getValue();
        projects.push(res);
        this._PROJECTS.next(projects);
        this.router.navigate(['/']);
      });

    return obs;
  }

  updateProject(updatedP: any): Observable <any>{
    const obs: Observable <any> = this.projectService.updateProject(updatedP);

    obs.subscribe(
      res => {
        const projects: any[] = this._PROJECTS.getValue();
        const index = projects.findIndex((project) => project.id === updatedP.id);
        projects[index] = res;
        this._PROJECTS.next(projects);
        this.router.navigate(['/']);
      }
    );

    return obs;
  }

  deleteProject(deletedP: any): Observable <any> {
    const obs: Observable <any> = this.projectService.removeProjects(deletedP.id);

    obs.subscribe(
      res => {
        const projects: any[] = this._PROJECTS.getValue();
        const index = projects.findIndex((project) => project.id === deletedP.id);
        projects.splice(index, 1);
        this._PROJECTS.next(projects);
      }
    );

    return obs;
  }


}
