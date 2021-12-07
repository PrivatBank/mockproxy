import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {RulesDataStoreService} from '../services/rules-data-store.service';
import {ProjectDataStoreService} from '../../projects/services/project-data-store.service';
import {take} from 'rxjs/operators';
import {RulesService} from '../services/rules.service';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmRemoveDialogComponent} from '../../shared/helpers/confirm-remove-dialog/confirm-remove-dialog.component';

@Component({
  selector: 'app-view-rules',
  templateUrl: './view-rules.component.html',
  styleUrls: ['./view-rules.component.scss']
})
export class ViewRulesComponent implements OnInit {

  // rules: any[] = [
  // {
  //   id: '608b041ebd86600008123828',
  //   name: 'Rule_11',
  //   description: 'Rule 11',
  //   state: 'active',
  //   timeWait: 0,
  //   requestMethod: 'POST',
  //   requestUrl: 'https://test.it.loc',
  //   fullBodyFit: false,
  //   requestBody: '{"name":"Duncan MacLeod"}',
  //   mockId: null,
  //   projectId: '608a999acd82bb68cfa27d0e'
  // }, {
  //   id: '608b0433bd86600008123829',
  //   name: 'Rule_12',
  //   description: 'Rule 12',
  //   state: 'disabled',
  //   timeWait: 0,
  //   requestMethod: 'POST',
  //   requestUrl: 'https://test.it.loc',
  //   fullBodyFit: false,
  //   requestBody: '{"name":"Duncan MacLeod"}',
  //   mockId: null,
  //   projectId: '608a999acd82bb68cfa27d0e'
  // }];
   rules: any[] = [];
  idApp: any = '';

  constructor(
    private router: Router,
    public rulesStore: RulesDataStoreService,
    public rulesService: RulesService,
    public projectStore: ProjectDataStoreService,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog
  ) {

    this.idApp = localStorage.getItem('selectedProjectId');

    // this.projectStore.getSelectedProject()
    //   .pipe( take(1) ).subscribe(
    //   (project: any) => {
    //     if (!project) {
    //       this.router.navigate(['/projects']);
    //     }
    //     this.idApp = project.id;
    //   }
    // );
  }


  ngOnInit(): void {

    this.rulesService.getRules(this.idApp).pipe(take(1))
      .subscribe(
        (res: any[]) => {
          this.rules = res;
        },
        (err: any) => console.log('Error retrieving Rules')
      );
  }

  addRule(): void {
    this.rulesStore.setSelectedRule(null);
    this.router.navigate([`/details-project/${this.idApp}/add-rule`]);
  }

  editRule(rule: any): void {
    this.rulesStore.setSelectedRule(rule);
    this.router.navigate([`/details-project/${this.idApp}/add-rule`]);
  }

  removeRule(rule: any): void {
    const dialogRef = this.dialog.open(ConfirmRemoveDialogComponent, {
      width: '450px',
      data: 'Do you want to remove the rule?'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.rulesService.removeRule(rule.id).pipe(take(1))
          .subscribe(
            (res: any[]) => {
              this.rules = this.rules.filter( r => r.id !== rule.id);
            },
            (err: any) => console.log('Error removing Rules')
          );
      }
    });
  }

  changeState(rule: any, event: any): void {
    if (event.checked) {
      rule.state = 'active';
    } else {
      rule.state = 'disabled';
    }

    this.rulesService.updateStateRule( rule.id, rule.state).pipe(take(1))
      .subscribe(
        (res: any[]) => {
          this.rulesStore.setSelectedRule(rule);
          console.log( 'Update rule succsess', res);
        },
        (err: any) => console.log('Error removing Rules')
      );
  }

}
