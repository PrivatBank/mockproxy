import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {RulesDataStoreService} from '../../rules/services/rules-data-store.service';
import {MockService} from '../services/mock.service';
import {take} from 'rxjs/operators';
import {MockDataStoreService} from '../services/mock-data-store.service';
import {ConfirmRemoveDialogComponent} from "../../shared/helpers/confirm-remove-dialog/confirm-remove-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-view-moks',
  templateUrl: './view-moks.component.html',
  styleUrls: ['./view-moks.component.scss']
})
export class ViewMoksComponent implements OnInit {

  // mocks: any[] = [
  //     {
  //       id: '6086c495025f5746ff2673c1',
  //       projectId: '6081f0f7c733683ec98386d2',
  //       name: 'Test mock 2',
  //       description: 'Some text about mock',
  //       state: 'active',
  //       response: {
  //         code: 200,
  //         headers: [{
  //           name: 'content-type',
  //           value: 'Json'
  //         },
  //           {
  //             name: 'content-t44ype',
  //             value: 'Jswefon'
  //           }
  //         ],
  //         body: {
  //           projectId: '6081f0f7c733683ec98386d2',
  //           name: 'Test mock',
  //         }
  //       }
  //     },
  //     {
  //       id: '608ba912cefb2f3c7e84bc1a',
  //       projectId: '6081f0f7c733683ec98386d2',
  //       name: 'Test mock',
  //       description: 'Some text about mock',
  //       state: 'active',
  //       response: {
  //         code: 200,
  //         headers: [{
  //             name: 'content-t44ype',
  //             value: 'Jswefon'
  //         }],
  //         body: '{a:\'foo\', \'b\':\'bar\', c:[false,2,null, \'null\']}'
  //       }
  //     },
  //   {
  //     id: '608ba912cefb2f3c7e84bc1a',
  //     projectId: '6081f0f7c733683ec98386d2',
  //     name: 'Test mock',
  //     description: 'Some text about mock',
  //     state: 'active',
  //     response: {
  //       code: 200,
  //       headers: [{
  //         name: 'content-t44ype',
  //         value: 'Jswefon'
  //       }],
  //       body: '{"projectId": "6081f0f7c733683ec98386d2", "name": "Test mock"}'
  //     }
  //   }
  //   ];
   mocks: any[] = [];
  idApp: any = '';

  constructor(
    private router: Router,
    public mockService: MockService,
    private activatedRoute: ActivatedRoute,
    public mockStore: MockDataStoreService,
    public dialog: MatDialog
  ) {
    this.idApp = localStorage.getItem('selectedProjectId');
  }


  ngOnInit(): void {

    this.mockService.getMocks(this.idApp).pipe(take(1))
      .subscribe(
        (res: any[]) => {
          this.mocks = res;
        },
        (err: any) => console.log('Error retrieving Mocks')
      );
  }

  addMock(): void {
    this.mockStore.setSelectedMock(null);
    this.router.navigate(['../add-mock'], {relativeTo: this.activatedRoute});
  }

  editMock(mock: any): void {
    this.mockStore.setSelectedMock(mock);
    this.router.navigate(['../add-mock'], {relativeTo: this.activatedRoute});
  }

  removeMock(mock: any): void {

    const dialogRef = this.dialog.open(ConfirmRemoveDialogComponent, {
      width: '450px',
      data: 'Do you want to remove the mock?'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.mockService.removeMock(mock.id).pipe(take(1))
          .subscribe(
            (res: any[]) => {
              this.mocks = this.mocks.filter( m => m.id !== mock.id);
            },
            (err: any) => console.log('Error removing Rules')
          );
      }
    });
  }

}
