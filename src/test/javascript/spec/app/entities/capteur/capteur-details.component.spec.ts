/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CapteurDetailComponent from '@/entities/capteur/capteur-details.vue';
import CapteurClass from '@/entities/capteur/capteur-details.component';
import CapteurService from '@/entities/capteur/capteur.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Capteur Management Detail Component', () => {
    let wrapper: Wrapper<CapteurClass>;
    let comp: CapteurClass;
    let capteurServiceStub: SinonStubbedInstance<CapteurService>;

    beforeEach(() => {
      capteurServiceStub = sinon.createStubInstance<CapteurService>(CapteurService);

      wrapper = shallowMount<CapteurClass>(CapteurDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { capteurService: () => capteurServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCapteur = { id: 123 };
        capteurServiceStub.find.resolves(foundCapteur);

        // WHEN
        comp.retrieveCapteur(123);
        await comp.$nextTick();

        // THEN
        expect(comp.capteur).toBe(foundCapteur);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCapteur = { id: 123 };
        capteurServiceStub.find.resolves(foundCapteur);

        // WHEN
        comp.beforeRouteEnter({ params: { capteurId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.capteur).toBe(foundCapteur);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
