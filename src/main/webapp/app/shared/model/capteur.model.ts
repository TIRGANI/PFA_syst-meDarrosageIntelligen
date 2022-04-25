import { IBrache } from '@/shared/model/brache.model';

export interface ICapteur {
  id?: number;
  type?: string;
  image?: string | null;
  description?: string | null;
  braches?: IBrache[] | null;
}

export class Capteur implements ICapteur {
  constructor(
    public id?: number,
    public type?: string,
    public image?: string | null,
    public description?: string | null,
    public braches?: IBrache[] | null
  ) {}
}
