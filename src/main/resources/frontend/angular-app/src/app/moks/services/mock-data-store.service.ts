import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {RulesService} from "../../rules/services/rules.service";

@Injectable({
  providedIn: 'root'
})
export class MockDataStoreService {

  public rulesMock: any[] = [];

  private _MOCKS = new BehaviorSubject<any[]>([]);
  private selectedMock = new BehaviorSubject<any>(null);

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    // this.loadInitialData();
  }

  getSelectedMock(): Observable<any> {
    return this.selectedMock.asObservable();
  }

  setSelectedMock(mock: any): void {
    return this.selectedMock.next(mock);
  }


  get rules(): Observable<any[]> {
    return this._MOCKS.asObservable();
  }
}
