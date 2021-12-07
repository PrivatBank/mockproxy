import { Component, OnInit } from '@angular/core';
import {ActivationEnd, NavigationEnd, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-project-details-main-conteiner',
  templateUrl: './project-details-main-conteiner.component.html',
  styleUrls: ['./project-details-main-conteiner.component.scss']
})
export class ProjectDetailsMainConteinerComponent implements OnInit {

  public activeLink = '';
  public navigationEnd: any;
  public urls: any = {
    rules: 'rules',
    mocks: 'mocks',
    proxy: 'proxy',
    settings: 'settings',
    '': ''
  };

  constructor(
    public router: Router
  ) {}

  ngOnInit(): void {

  }

  public setActiveUrl(url: string): void {
    const firstUrl = url.split('/')[1];
    if (this.urls.hasOwnProperty(firstUrl)) {
      this.activeLink = this.urls[firstUrl];
    }
  }

}
