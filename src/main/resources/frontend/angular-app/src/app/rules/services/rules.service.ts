import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RulesService {

  private getRulesUrl = '/rule/all/';
  private createRuleUrl = '/rule';
  private removeRuleUrl = '/rule/';
  private changeStateRuleUrl = '/rule/';
  private appId: any = '';

  constructor(
    private http: HttpClient,
    private activatedRoute: ActivatedRoute
  ) {
    this.appId = this.activatedRoute.snapshot.paramMap.get('idProject');
    console.log('this.appId in Dervice__', this.appId);
  }

  getRules(appId: string): any {
    return this.http.get<any>(this.getRulesUrl + appId);
  }

  createRule(rule: any): any {
    return this.http.post<any>(this.createRuleUrl, rule);
  }

  updateRule(rule: any): any {
    return this.http.put<any>(this.createRuleUrl, rule);
  }

  updateStateRule(idRule: any, state: any): any {
    return this.http.put<any>(this.changeStateRuleUrl + idRule + '/state/' + state, {});
  }

  removeRule(idRule: any): any {
    return this.http.delete<any>(this.removeRuleUrl + idRule);
  }
}
