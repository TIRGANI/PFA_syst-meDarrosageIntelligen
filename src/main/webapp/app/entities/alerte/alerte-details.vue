<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="alerte">
        <h2 class="jh-entity-heading" data-cy="alerteDetailsHeading">
          <span v-text="$t('pfaApp.alerte.detail.title')">Alerte</span> {{ alerte.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.alerte.humidite')">Humidite</span>
          </dt>
          <dd>
            <span>{{ alerte.humidite }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.alerte.temperature')">Temperature</span>
          </dt>
          <dd>
            <span>{{ alerte.temperature }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.alerte.luminosite')">Luminosite</span>
          </dt>
          <dd>
            <span>{{ alerte.luminosite }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.alerte.parcelle')">Parcelle</span>
          </dt>
          <dd>
            <div v-if="alerte.parcelle">
              <router-link :to="{ name: 'ParcelleView', params: { parcelleId: alerte.parcelle.id } }">{{ alerte.parcelle.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="alerte.id" :to="{ name: 'AlerteEdit', params: { alerteId: alerte.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alerte-details.component.ts"></script>
