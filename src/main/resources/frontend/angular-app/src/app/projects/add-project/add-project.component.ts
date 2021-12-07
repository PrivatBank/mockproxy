import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {debounceTime, filter, map, retry, startWith, switchMap, take} from 'rxjs/operators';
import {Router} from '@angular/router';
import {ProjectService} from '../services/project.service';
import {of} from 'rxjs';
import {ProjectDataStoreService} from '../services/project-data-store.service';

@Component({
  selector: 'app-add-project',
  templateUrl: './add-project.component.html',
  styleUrls: ['./add-project.component.scss']
})
export class AddProjectComponent implements OnInit {

  userListMOCK: any[] = [
    {
      login: 'dn291092sav',
      name: 'Шкабара Олександр Володимирович',
      photoUrl: 'https://staff.privatbank.ua/photo/dn291092sav'
    },
    {
      login: 'dn110191sav1',
      name: 'Шкабара Анатолій Володимирович',
      photoUrl: 'https://staff.privatbank.ua/photo/dn110191sav1'
    }
  ];
  selectedProject: any;
  userListFiltered: any[] = [];
  editForm: FormGroup;

  get name(): FormControl {
    return this.editForm.get('name') as FormControl;
  }

  get description(): FormControl {
    return this.editForm.get('description') as FormControl;
  }



  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private projectcService: ProjectService,
    public projectStore: ProjectDataStoreService
  ) {

    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.projectStore.getSelectedProject()
        .pipe( take(1) ).subscribe(
      (project: any) => {
        console.log(project);
        this.selectedProject = project;
        if (this.selectedProject){
          this.patchForm(this.selectedProject);
        }
      }
    );
  }

  patchForm(data: any): void {
    const convertedData = {...data};
    this.editForm.patchValue(convertedData, {emitEvent: false, onlySelf: true});
    this.editForm.updateValueAndValidity();
  }

  convertedValue(foundObj: any): any {
    return foundObj ? foundObj.name : null;
  }

  onSubmit(): void {

    if ( !this.editForm.valid) {
      return;
    }
    const app = this.editForm.value;

    if (this.selectedProject){
      app.id = this.selectedProject.id;
      this.projectStore.updateProject(app);
    } else {
      this.projectStore.addProject(app);
    }


  }

  cancel(): void {
    this.router.navigate(['/']);
  }

  back(): void {
    history.back();
  }

}
