import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {RulesService} from './rules.service';


@Injectable({
  providedIn: 'root'
})
export class RulesDataStoreService {

  public prulesMock: any[] = [{
    id: '608b041ebd86600008123828',
    name: 'Rule_11',
    description: 'Rule 11',
    state: 'active',
    timeWait: 0,
    requestMethod: 'POST',
    requestUrl: 'https://test.it.loc',
    fullBodyFit: false,
    requestBody: '{"name":"Duncan MacLeod"}',
    mockId: null,
    projectId: '608a999acd82bb68cfa27d0e'
  }, {
    id: '608b0433bd86600008123829',
    name: 'Rule_12',
    description: 'Rule 12',
    state: 'active',
    timeWait: 0,
    requestMethod: 'POST',
    requestUrl: 'https://test.it.loc',
    fullBodyFit: false,
    requestBody: '{"name":"Duncan MacLeod"}',
    mockId: null,
    projectId: '608a999acd82bb68cfa27d0e'
  }];

  private _RULES = new BehaviorSubject<any[]>([]);
  private selectedRule = new BehaviorSubject<any>(null);

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public rulesService: RulesService
  ) {
    // this.loadInitialData();
  }

  getSelectedRule(): Observable<any> {
    return this.selectedRule.asObservable();
  }

  setSelectedRule(project: any): void {
    return this.selectedRule.next(project);
  }


  get rules(): Observable<any[]> {
    return this._RULES.asObservable();
  }

  getProjectsList(): any[] {
    return this._RULES.getValue();
  }

  // loadInitialData(): void {
  //   if (this.prulesMock.length > 0) {
  //     this._RULES.next(this.prulesMock);
  //   }
  //   this.rulesService.getRules()
  //     .subscribe(
  //       (res: any[]) => {
  //         this.prulesMock = res;
  //         console.log('__this.prulesMock__', this.prulesMock);
  //         this._RULES.next(this.prulesMock);
  //       },
  //       (err: any) => console.log('Error retrieving Projects')
  //     );
  // }

  addRule_(newRule: any): Observable <any> {

    const obs: Observable <any>  = this.rulesService.createRule(newRule);

    obs.subscribe(
      res => {
        const projects: any[] = this._RULES.getValue();
        projects.push(res);
        this._RULES.next(projects);
        this.router.navigate(['/']);
      });

    return obs;
  }

  updateProject(updatedP: any): Observable <any>{
    const obs: Observable <any> = this.rulesService.updateRule(updatedP);

    obs.subscribe(
      res => {
        const projects: any[] = this._RULES.getValue();
        const index = projects.findIndex((project) => project.id === updatedP.id);
        projects[index] = res;
        this._RULES.next(projects);
        this.router.navigate(['/']);
      }
    );

    return obs;
  }

  deleteProject(deletedR: any): Observable <any> {
    const obs: Observable <any> = this.rulesService.removeRule(deletedR.id);

    obs.subscribe(
      res => {
        const projects: any[] = this._RULES.getValue();
        const index = projects.findIndex((project) => project.id === deletedR.id);
        projects.splice(index, 1);
        this._RULES.next(projects);
      }
    );

    return obs;
  }
}
