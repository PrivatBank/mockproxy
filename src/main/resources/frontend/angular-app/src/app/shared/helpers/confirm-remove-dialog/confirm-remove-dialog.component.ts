import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-remove-dialog',
  templateUrl: './confirm-remove-dialog.component.html',
  styleUrls: ['./confirm-remove-dialog.component.scss']
})
export class ConfirmRemoveDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmRemoveDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public message: string
  ) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
