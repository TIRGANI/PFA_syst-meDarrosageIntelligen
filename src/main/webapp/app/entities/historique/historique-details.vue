<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="historique">
        <h2 class="jh-entity-heading" data-cy="historiqueDetailsHeading">
          <span v-text="$t('pfaApp.historique.detail.title')">Historique</span> {{ historique.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.historique.dateArosage')">Date Arosage</span>
          </dt>
          <dd>
            <span>{{ historique.dateArosage }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.historique.qttEau')">Qtt Eau</span>
          </dt>
          <dd>
            <span>{{ historique.qttEau }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.historique.parcelle')">Parcelle</span>
          </dt>
          <dd>
            <div v-if="historique.parcelle">
              <router-link :to="{ name: 'ParcelleView', params: { parcelleId: historique.parcelle.id } }">{{
                historique.parcelle.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="historique.id"
          :to="{ name: 'HistoriqueEdit', params: { historiqueId: historique.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historique-details.component.ts"></script>
