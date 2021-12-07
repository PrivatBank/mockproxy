import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {RulesDataStoreService} from '../services/rules-data-store.service';
import {RulesService} from '../services/rules.service';
import {ProjectDataStoreService} from '../../projects/services/project-data-store.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_RADIO_DEFAULT_OPTIONS} from '@angular/material/radio';
import {take} from 'rxjs/operators';
import {MockService} from '../../moks/services/mock.service';

@Component({
  selector: 'app-add-rule',
  templateUrl: './add-rule.component.html',
  styleUrls: ['./add-rule.component.scss'],
  providers: [{
    provide: MAT_RADIO_DEFAULT_OPTIONS,
    useValue: { color: 'accent' },
  }]
})
export class AddRuleComponent implements OnInit {
  selectedRule: any;
  appId: any = '';
  editForm: FormGroup;
  mocks: any[] = [];


  // "id": "608b041ebd86600008123828",
  // "name": "Rule_11",
  // "description": "Rule 11",
  // "state": "active",
  // "timeWait": 0,
  // "requestMethod": "POST",
  // "requestUrl": "https://test.it.loc",
  // "fullBodyFit": false,
  // "requestBody": "{\"name\":\"Duncan MacLeod\"}",
  // "mockId": null,
  // "projectId": "608a999acd82bb68cfa27d0e"

  get name(): FormControl {
    return this.editForm.get('name') as FormControl;
  }

  get description(): FormControl {
    return this.editForm.get('description') as FormControl;
  }

  get state(): FormControl {
    return this.editForm.get('state') as FormControl;
  }

  get timeWait(): FormControl {
    return this.editForm.get('timeWait') as FormControl;
  }

  get requestMethod(): FormControl {
    return this.editForm.get('requestMethod') as FormControl;
  }

  get requestUrl(): FormControl {
    return this.editForm.get('requestUrl') as FormControl;
  }

  get fullBodyFit(): FormControl {
    return this.editForm.get('fullBodyFit') as FormControl;
  }

  get requestBody(): FormControl {
    return this.editForm.get('requestBody') as FormControl;
  }

  get mockId(): FormControl {
    return this.editForm.get('mockId') as FormControl;
  }

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    public rulesStore: RulesDataStoreService,
    public rulesService: RulesService,
    public projectStore: ProjectDataStoreService,
    public mockService: MockService
  ) {
    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: [''],
      state: ['active'],
      timeWait: [''],
      requestMethod: [''],
      requestUrl: [''],
      fullBodyFit: [false],
      requestBody: [''],
      mockId: ['']
    });


  }



  ngOnInit(): void {
    this.appId = localStorage.getItem('selectedProjectId');
    // this.projectStore.getSelectedProject().subscribe(
    //   rez => {
    //     this.appId = rez;
    //   }
    // );
    this.mockService.getMocks(this.appId).pipe(take(1))
      .subscribe(
        (rez: any) => {
          this.mocks = rez;
        },
        (err: any) => { }
      );

    this.rulesStore.getSelectedRule().subscribe(
      rule => {
        this.selectedRule = rule;
        if (this.selectedRule){
          this.patchForm(this.selectedRule);
        }
      },
      error => {}
    );
  }

  patchForm(data: any): void {
    const convertedData = {...data};

    // if (convertedData.hasOwnProperty('state')) {
    //   convertedData.state = convertedData.state === 'active' ? true : false;
    // }
    this.editForm.patchValue(convertedData, {emitEvent: false, onlySelf: true});
    this.editForm.updateValueAndValidity();
  }


  onSubmit(): void {
    const formValueRule = this.editForm.value;
    // formValueRule.fullBodyFit = formValueRule.fullBodyFit ? 'full' : 'part';
    formValueRule.projectId = this.appId;
    formValueRule.state = formValueRule.state ? 'active' : 'disabled';
    const newRule: any = this.cleanObject(formValueRule);
    newRule.fullBodyFit = formValueRule.fullBodyFit;
    console.log(newRule);
    if (this.selectedRule){
      newRule.id = this.selectedRule.id;
      console.log('__Submit_edit_', newRule);
      this.rulesService.updateRule(newRule).pipe(take(1))
        .subscribe(
          (rez: any) => {
            this.router.navigate([`/details-project/${this.appId}/rules`]);
          },
          (err: any) => { }
        );
    } else {
      console.log('__Submit_new', newRule);
      this.rulesService.createRule(newRule).pipe(take(1))
        .subscribe(
          (rez: any) => {
            this.router.navigate([`/details-project/${this.appId}/rules`]);
          },
          (err: any) => { }
        );
    }

  }

  private cleanObject(myObj: any): void {
    return Object.entries(myObj).reduce((acc: any, [key, val]) => {
        if (val) { acc[key] = val; }
        return acc;
      }, {});
  }

  cancel(): void {
    this.router.navigate([`/details-project/${this.appId}/rules`]);
  }
}
