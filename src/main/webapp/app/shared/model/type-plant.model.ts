import { IPlante } from '@/shared/model/plante.model';

export interface ITypePlant {
  id?: number;
  lebelle?: string;
  humiditeMax?: number;
  humiditeMin?: number;
  temperature?: number;
  luminisite?: number;
  plante?: IPlante | null;
}

export class TypePlant implements ITypePlant {
  constructor(
    public id?: number,
    public lebelle?: string,
    public humiditeMax?: number,
    public humiditeMin?: number,
    public temperature?: number,
    public luminisite?: number,
    public plante?: IPlante | null
  ) {}
}
