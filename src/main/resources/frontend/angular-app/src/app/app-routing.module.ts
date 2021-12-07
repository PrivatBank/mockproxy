import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ViewProjectListComponent} from './projects/view-project-list/view-project-list.component';
import {AddProjectComponent} from './projects/add-project/add-project.component';
import {ProjectDetailsMainConteinerComponent} from './project-details-main-conteiner/project-details-main-conteiner.component';
import {ViewMoksComponent} from './moks/view-moks/view-moks.component';
import {ViewRulesComponent} from './rules/view-rules/view-rules.component';
import {AddRuleComponent} from './rules/add-rule/add-rule.component';
import {AddMockComponent} from './moks/add-mock/add-mock.component';
import {ViewLogsComponent} from './logs/view-logs/view-logs.component';

const routes: Routes = [
  {path: '', component: ViewProjectListComponent},
  { path: 'projects', component:  ViewProjectListComponent},
  { path: 'add-project', component:  AddProjectComponent},
  { path: 'details-project/:idProject',
    component:  ProjectDetailsMainConteinerComponent,
    children: [
      {
        path: '',
        component: ViewRulesComponent,
      },
      {
        path: 'rules',
        component: ViewRulesComponent,
      },
      { path: 'add-rule', component:  AddRuleComponent},
      {
        path: 'mocks',
        component: ViewMoksComponent
      },
      { path: 'add-mock', component:  AddMockComponent},
      {
        path: 'logs',
        component: ViewLogsComponent
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
