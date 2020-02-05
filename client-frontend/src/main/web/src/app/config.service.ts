import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  readonly config: Promise<any>;

  constructor(private http: HttpClient) {
    this.config = this.http.get('assets/config.json').toPromise();
  }

}
