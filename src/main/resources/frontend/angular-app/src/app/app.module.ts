import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from './shared/modules/material/material.module';
import { HeaderComponent } from './navigations/header/header.component';
import { ViewProjectListComponent } from './projects/view-project-list/view-project-list.component';
import { AddProjectComponent } from './projects/add-project/add-project.component';
import { ProjectDetailsMainConteinerComponent } from './project-details-main-conteiner/project-details-main-conteiner.component';
import { ViewRulesComponent } from './rules/view-rules/view-rules.component';
import { ViewMoksComponent } from './moks/view-moks/view-moks.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AddRuleComponent } from './rules/add-rule/add-rule.component';
import { AddMockComponent } from './moks/add-mock/add-mock.component';
import { ViewLogsComponent } from './logs/view-logs/view-logs.component';
import { ConfirmRemoveDialogComponent } from './shared/helpers/confirm-remove-dialog/confirm-remove-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    ViewProjectListComponent,
    AddProjectComponent,
    ProjectDetailsMainConteinerComponent,
    ViewRulesComponent,
    ViewMoksComponent,
    AddRuleComponent,
    AddMockComponent,
    ViewLogsComponent,
    ConfirmRemoveDialogComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [
    ConfirmRemoveDialogComponent
  ]
})
export class AppModule { }
