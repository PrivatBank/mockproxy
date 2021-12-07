import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class MockService {

  private getMocksUrl = '/mock/project-id/';
  private createMockUrl = '/mock';
  private removeMockUrl = '/mock/';
  private appId: any = '';

  constructor(
    private http: HttpClient,
    private activatedRoute: ActivatedRoute
  ) {
    this.appId = this.activatedRoute.snapshot.paramMap.get('idProject');
    console.log('this.appId in Dervice__', this.appId);
  }

  getMocks(appId: string): any {
    return this.http.get<any>(this.getMocksUrl + appId);
  }

  createMock(mock: any): any {
    return this.http.post<any>(this.createMockUrl, mock);
  }

  updateMock(mock: any): any {
    return this.http.put<any>(this.createMockUrl, mock);
  }

  removeMock(idMock: any): any {
    return this.http.delete<any>(this.removeMockUrl + idMock);
  }
}
