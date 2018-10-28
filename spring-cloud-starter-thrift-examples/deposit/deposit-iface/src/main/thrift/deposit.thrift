namespace java io.ostenant.rpc.thrift.examples.thrift

enum ThriftRegion {
   NORTH = 1,
   CENTRAL = 2,
   SOUTH = 3,
   EAST = 4,
   SOUTHWEST = 5,
   NORTHWEST = 6,
   NORTHEAST = 7
}

enum ThriftDepositStatus {
   FINISHED = 1,
   PROCCEDING = 2,
   FAILED = 3
}

enum ThriftWithdrawStatus {
   FINISHED = 1,
   PROCCEDING = 2,
   FAILED = 3
}

struct ThriftBank {
   1: required i64 id,
   2: required string code,
   3: required string name,
   4: optional string description,
   5: optional map<ThriftRegion, list<ThriftBranch>> branches
}

struct ThriftBranch {
   1: required i64 id,
   2: required string code,
   3: required string name,
   4: required string address,
   5: optional i32 staffs,
   6: optional ThriftBank bank,
   7: optional ThriftRegion region
}

struct ThriftCustomer {
   1: required string IDNumber,
   2: required string name,
   3: required string birthday,
   4: required i32 sex = 0,
   5: required i32 age,
   6: optional list<string> address,
   7: optional set<ThriftDepositCard> depositCards
}

struct ThriftDepositCard {
   1: required string id,
   2: required bool isVip,
   3: required string openingTime,
   4: required double accountBalance,
   5: optional double accountFlow,
   6: optional ThriftBranch branch,
   7: optional ThriftCustomer customer,
   8: optional list<ThriftDeposit> depositHistory,
   9: optional list<ThriftWithdraw> WithdrawHistory
}

struct ThriftDeposit {
   1: required string serialNumber,
   2: required double transactionAmount,
   3: required string submittedTime,
   4: optional string finishedTime,
   5: optional ThriftDepositStatus status,
   6: optional ThriftDepositCard depositCard
}

struct ThriftWithdraw {
   1: required string serialNumber,
   2: required double transactionAmount,
   3: required string submittedTime,
   4: optional string finishedTime,
   5: optional ThriftWithdrawStatus status,
   6: optional ThriftDepositCard depositCard
}

service ThriftBankService {
   void registerNewBank(ThriftBank bank);
   list<ThriftBank> queryAllBanks();
   ThriftBank getBankById(i64 bankId);
   map<ThriftRegion, list<ThriftBranch>> queryAllBranchesByRegion(i64 bankId);
}

service ThriftBranchService {
   void addNewBranch(i64 bankId, ThriftBranch branch);
   list<ThriftBranch> queryAllBranches(i64 bankId);
   ThriftBranch getBranchById(i64 branchId);
}

service ThriftCustomerService {
   ThriftCustomer getCustomerById(string customerId);
   list<ThriftCustomer> queryAllCustomers();
   void addNewUser(ThriftCustomer customer);
   void modifyUserById(string customerId, ThriftCustomer customer);
   i32 getTotalDepositCard(string customerId);

}

service ThriftDepositCardService {
   set<ThriftDepositCard> queryAllDepositCards(string customerId);
   void addNewDepositCard(string customerId, ThriftDepositCard depositCard);
   ThriftDepositStatus depositMoney(string depositCardId, double money);
   ThriftWithdrawStatus withdrawMoney(string depositCardId, double money);
   list<ThriftDeposit> queryDepositHistorys(string depositCardId);
   list<ThriftWithdraw> queryWithdrawHistorys(string depositCardId);
}

