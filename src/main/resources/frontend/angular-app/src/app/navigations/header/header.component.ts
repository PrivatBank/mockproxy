import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  fio = 'Admin admin';
  photo: any;
  urlFoto: any;

  constructor() { }

  ngOnInit(): void {
  }

  // tslint:disable-next-line:typedef
  goToInstruction() {
    window.open('https://confluence.privatbank.ua/pages/viewpage.action?pageId=244288560');
  }

}
