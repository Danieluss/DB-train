import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from 'src/environments/environment';
import {ConfigService} from "../config.service";
import {Ticket} from "./dto/ticket";
import {CommutationTicket, PreCommutationTicket} from "./dto/commutation-ticket";
import {PathTicket} from "./dto/path-ticket";
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

  commutationTicketTypes: CommutationTicket[] = [];
  discounts: Map<number, Discount> = new Map<number, Discount>();
  zones: Map<number, Zone> = new Map<number, Zone>();
  pathTicketSubject: ReplaySubject<PathTicket> = new ReplaySubject<PathTicket>();
  commutationTicketSubject: ReplaySubject<CommutationTicket> = new ReplaySubject<CommutationTicket>();


}
