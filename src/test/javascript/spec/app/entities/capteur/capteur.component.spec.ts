/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CapteurComponent from '@/entities/capteur/capteur.vue';
import CapteurClass from '@/entities/capteur/capteur.component';
import CapteurService from '@/entities/capteur/capteur.service';
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
  describe('Capteur Management Component', () => {
    let wrapper: Wrapper<CapteurClass>;
    let comp: CapteurClass;
    let capteurServiceStub: SinonStubbedInstance<CapteurService>;

    beforeEach(() => {
      capteurServiceStub = sinon.createStubInstance<CapteurService>(CapteurService);
      capteurServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CapteurClass>(CapteurComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          capteurService: () => capteurServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      capteurServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCapteurs();
      await comp.$nextTick();

      // THEN
      expect(capteurServiceStub.retrieve.called).toBeTruthy();
      expect(comp.capteurs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      capteurServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(capteurServiceStub.retrieve.callCount).toEqual(1);

      comp.removeCapteur();
      await comp.$nextTick();

      // THEN
      expect(capteurServiceStub.delete.called).toBeTruthy();
      expect(capteurServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
