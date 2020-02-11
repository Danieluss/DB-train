import {Discount} from "./discount";

export class PreTicket {

  uuid: string;
  discount: number;

}

export class Ticket {

  uuid: string;
  discount: Discount;

}
