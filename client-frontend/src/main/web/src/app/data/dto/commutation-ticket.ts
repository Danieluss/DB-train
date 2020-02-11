import {PreTicket, Ticket} from "./ticket";
import {CommutationTicketType} from "./commutation-ticket-type";

export interface PreCommutationTicket extends PreTicket {
  startDate: string;
  endDate: string;
  type: number;
}

export interface CommutationTicket extends Ticket {
  startDate: string;
  endDate: string;
  type: CommutationTicketType;
}
