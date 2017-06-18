import { Component, Input, OnInit } from '@angular/core';

import { DetailViewService } from '../detail.service';

export interface BorrowDate {
  propertyName : string;
  displayText : string;
  displayIcon : string;
}

@Component({
  selector: 'view-detail-borrow-date',
  templateUrl: './borrow-date.component.html',
  styleUrls: [ './borrow-date.component.css' ]
})
export class BorrowDateViewComponent implements OnInit {

  @Input() borrowDate : BorrowDate;

  public borrowDatePropertyName : string;

  public borrowDateDisplayText : string;

  public borrowIconName : string;

  public isInputAvailable : boolean = false;

  public isDataAvailable : boolean = false;

  public model : any;

  constructor(
    private detailViewService : DetailViewService
  ) {
    this.detailViewService.getModel().subscribe((model : any) => {
      this.model = model;
      this.isDataAvailable = true;
    });
  }

  public ngOnInit() : void {
    if (this.borrowDate) {
      this.borrowDateDisplayText = this.borrowDate.displayText;
      this.borrowDatePropertyName = this.borrowDate.propertyName;
      this.borrowIconName = this.borrowDate.displayIcon;
      this.isInputAvailable = true;
    }
  }

  getLocalizedTime(property : string) : string {
    let localTime : string = '';
    let date : Date = new Date(this.model[property]);
    localTime += date.getDay() + '.' + date.getMonth() + '.' + date.getFullYear();
    return localTime;
  }

}
