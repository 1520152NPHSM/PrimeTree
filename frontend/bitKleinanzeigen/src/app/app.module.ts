import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NetworkModule } from './network/network.module';
import { AppComponent }  from './app.component';
import { ListingRequest } from './network/listing.controller';
import { NetworkRequest } from './network/network.controller';
import { RESTService } from './network/rest.service';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { ListingFormsModule } from './forms/forms.module';

@NgModule({
  imports:      [ BrowserModule, FormsModule, NetworkModule, ListingFormsModule ],
  declarations: [ AppComponent ],
  bootstrap:    [ AppComponent ],
  providers: [ RESTService ]
})
export class AppModule { }
