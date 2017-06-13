import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { FilterCheckboxComponent } from './elements/filter-checkbox/filter-checkbox.component';
import { FilterMultiValueCheckboxComponent } from './elements/filter-multi-value-checkbox/filter-multi-value-checkbox.component';
import { FilterRadioComponent } from './elements/filter-radio/filter-radio.component';
import { LocationFilter } from './elements/filter-location/filter-location.component';
import { FilterListComponent } from './elements/filter-list/filter-list.component';
import { FilterGoogleMapsComponent } from './elements/filter-google-maps/filter-google-maps.component';

import { DescriptionFormComponent } from './elements/description/description.component';
import { ImageFormComponent } from './elements/image/image.component';
import { PriceFormComponent } from './elements/price/price.component';
import { TitleFormComponent } from './elements/title/title.component';
import { GalleryFormComponent } from './elements/gallery/gallery.component';
import { LocationFormComponent } from './elements/location/location.component';
import { ConditionFormComponent } from './elements/condition/condition.component';
import { ExpiryDateFormComponent } from './elements/expiry-date/expiry-date.component';
import { JourneyStopsFormComponent } from './elements/journey-stops/journey-stops.component';

/**
 * This module collects all components and services which pertain to building forms.
 */
@NgModule({
  declarations: [
    FilterCheckboxComponent,
    FilterRadioComponent,
    FilterMultiValueCheckboxComponent,
    LocationFilter,
    FilterListComponent,
    FilterGoogleMapsComponent,

    DescriptionFormComponent,
    ImageFormComponent,
    PriceFormComponent,
    TitleFormComponent,
    GalleryFormComponent,
    LocationFormComponent,
    ConditionFormComponent,
    ExpiryDateFormComponent,
    JourneyStopsFormComponent
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    LocationFilter,

    FilterCheckboxComponent,
    FilterRadioComponent,
    FilterMultiValueCheckboxComponent,
    FilterListComponent,
    FilterGoogleMapsComponent,

    DescriptionFormComponent,
    ImageFormComponent,
    PriceFormComponent,
    TitleFormComponent,
    GalleryFormComponent,
    LocationFormComponent,
    ConditionFormComponent,
    ExpiryDateFormComponent,
    JourneyStopsFormComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [  ]
})
export class FormModule {

}