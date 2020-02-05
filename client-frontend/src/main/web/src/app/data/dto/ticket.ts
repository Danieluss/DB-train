import {Discount} from "./discount";

export interface PreTicket {

  uuid: string;
  discount: number;

}

export interface Ticket {

  uuid: string;
  discount: Discount;

}
