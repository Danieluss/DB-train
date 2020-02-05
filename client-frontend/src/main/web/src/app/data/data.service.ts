import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from 'src/environments/environment';
import {ConfigService} from "../config.service";
import {Ticket} from "./dto/ticket";
import {CommutationTicket, PreCommutationTicket} from "./dto/commutation-ticket";
import {PathTicket, PrePathTicket} from "./dto/path-ticket";
import {stringify} from "querystring";
import {Observable, ReplaySubject} from "rxjs";
import {flatMap, map} from "rxjs/operators";
import {Discount} from "./dto/discount";
import {CommutationTicketType} from "./dto/commutation-ticket-type";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private base_url: string;

  pathTicketSubject: ReplaySubject<PathTicket> = new ReplaySubject<PathTicket>();
  commutationTicketSubject: ReplaySubject<CommutationTicket> = new ReplaySubject<CommutationTicket>();

  constructor(
    private configService: ConfigService,
    private http: HttpClient) {
    configService.config.then((cfg: any) => {
      this.base_url = cfg.server + cfg.port;
    });
  }

  getPathTickets(page: number, size: number): Promise<any> {
    var prePathTickets: PrePathTicket[];
    var pathTickets: PathTicket[];
    return this.getAll('x').toPromise();
  }

  getCommutationTickets(page: number, size: number): Observable<any> {
    return this.getPaged('commutationticket', page, size);
  }

  private getAll(name: string): Observable<any> {
    return this.http.get(this.base_url + '/api/' + name + '/list');
  }

  private getPaged(name: string, page: number, size: number): Observable<any> {
    let params = new HttpParams().set('pageNumber', stringify(page)).set('pageSize', stringify(size));
    return this.http.get(this.base_url + '/api/' + name + '/page', {params: params});
  }

  private get(id: any, name: string): Observable<any> {
    return this.http.get(this.base_url + '/api/' + name + `/get${stringify(id)}`);
  }

}
