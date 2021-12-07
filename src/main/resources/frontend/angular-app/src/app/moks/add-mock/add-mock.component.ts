import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {RulesDataStoreService} from '../../rules/services/rules-data-store.service';
import {RulesService} from '../../rules/services/rules.service';
import {ProjectDataStoreService} from '../../projects/services/project-data-store.service';
import {MockService} from '../services/mock.service';
import {take} from 'rxjs/operators';
import {MockDataStoreService} from '../services/mock-data-store.service';


@Component({
  selector: 'app-add-mock',
  templateUrl: './add-mock.component.html',
  styleUrls: ['./add-mock.component.scss']
})
export class AddMockComponent implements OnInit {

  selectedMock: any;
  appId: any = '';
  editForm: FormGroup;

  //
  // "id": "6086c495025f5746ff2673c1",
  // "projectId": "6081f0f7c733683ec98386d2",
  // "name": "Test mock 2",
  // "description": "Some text about mock",
  // "state": "active",
  // "response": {
  //   "code": 200,
  //   "headers": {
  //     "content-type": "text/html",
  //     "my-header-a": "value-a"
  //   },
  //   "body": "mocked body..."
  // }

  get name(): FormControl {
    return this.editForm.get('name') as FormControl;
  }

  get description(): FormControl {
    return this.editForm.get('description') as FormControl;
  }

  get code(): FormControl {
    return this.editForm.get('code') as FormControl;
  }

  get body(): FormControl {
    return this.editForm.get('body') as FormControl;
  }

  get headers(): FormArray {
    return this.editForm.get('headers') as FormArray;
  }

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    public mockStore: MockDataStoreService,
    public rulesService: RulesService,
    public projectStore: ProjectDataStoreService,
    public mockService: MockService
  ) {
    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: [''],
      code: ['', [Validators.required]],
      body: ['', []],
      headers: this.formBuilder.array([], [])
    });
  }

  ngOnInit(): void {
    this.appId = localStorage.getItem('selectedProjectId');


    this.mockStore.getSelectedMock().subscribe(
      rule => {
        this.selectedMock = rule;
        if (this.selectedMock){
          this.patchForm(this.selectedMock);
        }
        // else {
        //   this.headers.push(this.createItemHeader('', ''));
        // }
      },
      error => {}
    );
  }

  createItemHeader(name: string, value: string): FormGroup {
    return this.formBuilder.group({
      name: [name, []],
      value: [value, []]
    });
  }

  addHeader(): void {
    const header = this.formBuilder.group({
      name: ['', []],
      value: ['', []]
    });

    this.headers.push(header);
  }

  deleteHeader(index: number): void {
    this.headers.removeAt(index);
  }

 patchForm(data: any): void {
   const convertedData = {...data};
   if (convertedData.hasOwnProperty('response')) {
     if (convertedData.response.hasOwnProperty('code')) {
       this.editForm.controls.code.patchValue(convertedData.response.code, {emitEvent: false, onlySelf: true});
       this.editForm.updateValueAndValidity();
     }

     if (convertedData.response.hasOwnProperty('body')) {
       if (!this.isJson(convertedData.response.body)) {
         this.editForm.controls.body.patchValue( convertedData.response.body, {emitEvent: false, onlySelf: true});
         this.editForm.updateValueAndValidity();
       } else {
         let body = '';
         let json;
         try{
           json = JSON.parse(convertedData.response.body);
         } catch (e) {
           console.log (e);
         }

         body = JSON.stringify(json, undefined, 2);

         this.editForm.controls.body.patchValue( body, {emitEvent: false, onlySelf: true});
         this.editForm.updateValueAndValidity();
       }
     }


     if (convertedData.response.hasOwnProperty('headers')) {
       if (convertedData.response.headers && convertedData.response.headers.length > 0) {
         convertedData.response.headers.forEach((header: any) => {
           this.headers.push(this.createItemHeader(header.name, header.value));
         });
       }
     }

     this.editForm.patchValue(convertedData, {emitEvent: false, onlySelf: true});
     this.editForm.updateValueAndValidity();
   }
 }

   isJson(item: any): boolean {
      item = typeof item !== 'string'
        ? JSON.stringify(item)
        : item;

      try {
        item = JSON.parse(item);
      } catch (e) {
        return false;
      }

      if (typeof item === 'object' && item !== null) {
        return true;
      }

      return false;
   }


    onSubmit(): void {
    const formValueRule = this.editForm.value;

    const newMock: any = {
      name: formValueRule.name,
      response: {
        code: formValueRule.code,
        body: formValueRule.body
      }
    };

    if (formValueRule.headers.length > 0) {
      newMock.response.headers = formValueRule.headers;
    }

    if (formValueRule.description) {
      newMock.description = formValueRule.description;
    }

    if (this.selectedMock) {
      newMock.id = this.selectedMock.id;
      console.log('__Submit_edit_', newMock);
      this.mockService.updateMock(newMock).pipe(take(1))
        .subscribe(
          (rez: any) => {
            this.router.navigate(['../mocks'], {relativeTo: this.activatedRoute});
          },
          (err: any) => { }
        );
    } else {
      newMock.projectId = this.appId;
      console.log('__Submit_new', newMock);
      this.mockService.createMock(newMock).pipe(take(1))
        .subscribe(
          (rez: any) => {
            this.router.navigate(['../mocks'], {relativeTo: this.activatedRoute});
          },
          (err: any) => { }
        );
    }

  }

  cancel(): void {
    this.router.navigate(['../mocks'], {relativeTo: this.activatedRoute});
  }

}
