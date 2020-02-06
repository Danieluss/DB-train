import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CommutationTicket} from "./dto/commutation-ticket";
import {PathTicket, PrePathTicket} from "./dto/path-ticket";
import {stringify} from "querystring";
import {Observable, ReplaySubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private base_url: string;

  pathTicketSubject: ReplaySubject<PathTicket> = new ReplaySubject<PathTicket>();
  commutationTicketSubject: ReplaySubject<CommutationTicket> = new ReplaySubject<CommutationTicket>();

  constructor(
    private http: HttpClient) {
  }

  getPathTickets(page: number, size: number): Promise<PathTicket[]> {
    var prePathTickets: PrePathTicket[];
    var pathTickets: PathTicket[] = [];
    return this.getPaged('pathticket', page, size).toPromise()
      .then((data)=>{
        prePathTickets = data.content;
        prePathTickets.forEach((prePathTicket) => {
          var pathTicket = new PathTicket();
          pathTicket.date = prePathTicket.date;
          pathTicket.price = prePathTicket.price;
          pathTicket.uuid = prePathTicket.uuid;
          pathTickets.push(pathTicket);
        });
        return Promise.all(
          prePathTickets
            .map((prePathTicket) =>
              this.get(prePathTicket.stationConnection1, 'stationsconnections')));
      }).then((values: any[]) => {
        return Promise.all(
          values
            .map((stationConnection) =>
              this.get(stationConnection.station, 'station')));
      }).then((stations: any[]) => {
        for(let i = 0; i < stations.length; i++) {
          pathTickets[i].stationFrom = stations[i].name;
        }
        return Promise.resolve();
      }).then(() => {
        return Promise.all(
          prePathTickets
            .map((prePathTicket) =>
              this.get(prePathTicket.stationConnection2, 'stationsconnections')));
      }).then((values: any[]) => {
        return Promise.all(
          values
            .map((stationConnection) =>
              this.get(stationConnection.station, 'station')));
      }).then((stations: any[]) => {
        for(let i = 0; i < stations.length; i++) {
          pathTickets[i].stationTo = stations[i].name;
        }
        return Promise.resolve();
      }).then(() => {
        return Promise.all(
          prePathTickets
            .map((prePathTicket) =>
              this.get(prePathTicket.discount, 'discount')));
      }).then((discounts: any[]) => {
        for(let i = 0; i < discounts.length; i++) {
          pathTickets[i].discount = discounts[i];
        }
        return Promise.resolve(pathTickets);
      });
  }

  getCommutationTickets(page: number, size: number): Observable<any> {
    return this.getPaged('commutationticket', page, size);
  }

  private getAll(name: string): Observable<any> {
    return this.http.get(this.base_url + '/api/' + name + '/list');
  }

  private getPaged(name: string, page: number, size: number): Observable<any> {
    let params = new HttpParams().set('pageNumber', page.toString()).set('pageSize', size.toString());
    return this.http.get( 'http://localhost:8080/api/' + name + '/page', {params: params});
  }

  private get(id: any, name: string): Observable<any> {
    return this.http.get('http://localhost:8080/api/' + name + `/get${stringify(id)}`);
  }

}
