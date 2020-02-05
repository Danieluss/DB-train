import {PreTicket, Ticket} from "./ticket";
import {Discount} from "./discount";

export interface PrePathTicket extends PreTicket {
  price: number;
  date: string;
  stationConnection1: number;
  stationConnection2: number;
}

export interface PathTicket extends Ticket {
  price: number;
  date: string;
  stationFrom: string;
  stationTo: string;
}
