# DataLayer and service Chia 2.7.5 RPC contracts

## Date
2026-10-30

## Git Commit

ad1924526ce591d023a8048d17c1450e89b37f10

## Change Summary
Added standalone DataLayer, crawler, solver and timelord API support and current shared HTTP RPC operations. Reconciled farmer and harvester contracts, routing defects, pagination, metadata, optional fields and exact numeric domains against the pinned Chia 2.7.5 source.

## Files
- src/main/java/io/mindspice/jxch/rpc/NodeConfig.java
- src/main/java/io/mindspice/jxch/rpc/enums/ChiaService.java
- src/main/java/io/mindspice/jxch/rpc/enums/endpoints/{Common,DataLayer,Crawler,Solver,Farmer,Harvester,Daemon}.java
- src/main/java/io/mindspice/jxch/rpc/http/{ChiaAPI,RPCClient,DataLayerAPI,CrawlerAPI,SolverAPI,TimelordAPI,FarmerAPI,HarvesterAPI,DaemonClient}.java
- src/main/java/io/mindspice/jxch/rpc/schemas/{datalayer,crawler,solver,farmer,harvester,shared}/
- src/test/java/{Common,DataLayer,Services,RpcTransport}*Test.java

## Behavioral Impact
Exposes 35 DataLayer service routes, two crawler routes, solver state and shared-only timelord control, with paired byte/typed methods. Added version/network/logging operations using service-bound response metadata. Corrected DataLayer port to 8562 and added solver/timelord configuration. Exact integral request paths cover upstream numeric domains; scoped deserialization avoids silent float/string-to-integer coercion. Deprecated daemon surfaces fail explicitly because daemon commands require unsupported WebSocket transport. No new transport architecture was introduced.

## Specification Impact
Implements the common/service portion of specifications/rpc-compatibility.md. Source identity is ff71448437a137c7092775923d5ecbbe104433e7; unreleased main routes, simulator and live-node validation remain excluded.

## Risks
Corrected numeric accessor types may require caller updates. Arbitrary wallet/CLVM and plugin JSON remains caller-managed. No live-node or simulator evidence is claimed.

## Follow-up Items
Independent closure validation PASS: 260 focused checks, zero failures/errors/skips. Earlier BLOCKED evidence is retained. Final integration and 1.0.0 package gate remain required before merge.
