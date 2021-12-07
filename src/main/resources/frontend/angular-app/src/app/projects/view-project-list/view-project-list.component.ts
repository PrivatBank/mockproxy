import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {ProjectDataStoreService} from '../services/project-data-store.service';



@Component({
  selector: 'app-view-project-list',
  templateUrl: './view-project-list.component.html',
  styleUrls: ['./view-project-list.component.scss']
})
export class ViewProjectListComponent implements OnInit {

  constructor(
    private router: Router,
    public projectStore: ProjectDataStoreService
  ) { }

  ngOnInit(): void {

  }

  selectP(project: any): void {
    this.projectStore.setSelectedProject(project);
    localStorage.setItem('selectedProjectId', project.id);
    this.router.navigate(['/details-project/' + project.id + '/']);
  }


  addProject(): void {
    this.projectStore.setSelectedProject(null);
    this.router.navigate(['/add-project']);
  }

  removeProject(project: any): void {
    console.log(project);
    this.projectStore.deleteProject(project);
  }

  editProject(project: any): void {
    this.projectStore.setSelectedProject(project);
    this.router.navigate(['/add-project']);
  }
}
