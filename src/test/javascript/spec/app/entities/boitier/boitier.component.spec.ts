/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import BoitierComponent from '@/entities/boitier/boitier.vue';
import BoitierClass from '@/entities/boitier/boitier.component';
import BoitierService from '@/entities/boitier/boitier.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Boitier Management Component', () => {
    let wrapper: Wrapper<BoitierClass>;
    let comp: BoitierClass;
    let boitierServiceStub: SinonStubbedInstance<BoitierService>;

    beforeEach(() => {
      boitierServiceStub = sinon.createStubInstance<BoitierService>(BoitierService);
      boitierServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<BoitierClass>(BoitierComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          boitierService: () => boitierServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      boitierServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllBoitiers();
      await comp.$nextTick();

      // THEN
      expect(boitierServiceStub.retrieve.called).toBeTruthy();
      expect(comp.boitiers[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      boitierServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(boitierServiceStub.retrieve.callCount).toEqual(1);

      comp.removeBoitier();
      await comp.$nextTick();

      // THEN
      expect(boitierServiceStub.delete.called).toBeTruthy();
      expect(boitierServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
