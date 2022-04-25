/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AlerteComponent from '@/entities/alerte/alerte.vue';
import AlerteClass from '@/entities/alerte/alerte.component';
import AlerteService from '@/entities/alerte/alerte.service';
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
  describe('Alerte Management Component', () => {
    let wrapper: Wrapper<AlerteClass>;
    let comp: AlerteClass;
    let alerteServiceStub: SinonStubbedInstance<AlerteService>;

    beforeEach(() => {
      alerteServiceStub = sinon.createStubInstance<AlerteService>(AlerteService);
      alerteServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AlerteClass>(AlerteComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alerteService: () => alerteServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      alerteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAlertes();
      await comp.$nextTick();

      // THEN
      expect(alerteServiceStub.retrieve.called).toBeTruthy();
      expect(comp.alertes[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      alerteServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(alerteServiceStub.retrieve.callCount).toEqual(1);

      comp.removeAlerte();
      await comp.$nextTick();

      // THEN
      expect(alerteServiceStub.delete.called).toBeTruthy();
      expect(alerteServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
