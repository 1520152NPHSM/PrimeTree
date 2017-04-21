import { Component } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { ListingRequest } from './network/listing.controller';
import { NetworkRequest } from './network/network.controller';
import { RESTService } from './network/rest.service';

@Component({
  selector: 'bITKleinanzeigen',
  templateUrl: 'app.component.html',
  styleUrls: [ 'app.component.css' ],
  providers: [ ListingRequest, RESTService, NetworkRequest ]
})
export class AppComponent  {  }
