import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import Tooltip from "@material-ui/core/Tooltip";

const classes = {
    title: {
      marginBottom: '-100rem',
      marginTop: '1rem'
    },

    tableCell: {
       fontFamily: 'sans-serif',
    }
};

let empty = false;
const columns = [
  { id: 'name', label: 'Colleague', minWidth: 170 },
      {
        id: 'rank',
        //Size\u00a0(km\u00b2)
        label: '🔥 Helpfulness',
        minWidth: 170,
        align: 'center',
        format: (value) => value.toLocaleString(),
      },
  {
    id: 'coverages',
    label: '# of Fulfilled Shift Coverages',
    minWidth: 170,
    align: 'center',
    format: (value) => value.toLocaleString(),
  },
  {
    id: 'requests',
    //Size\u00a0(km\u00b2)
    label: '# of Fulfilled Shift Requests',
    minWidth: 170,
    align: 'center',
    format: (value) => value.toLocaleString(),
  },


];

function getHelpfulnessIcon(num) {
    if (num === -1) return "❄️"
    if (num === 0) return "";
    else return "🔥" + getHelpfulnessIcon(num - 1)
}

function getData(id) {
    const xhr = new XMLHttpRequest();

    const postParameters = {
        orgID: id
    };

    let workerMap = null;
    xhr.addEventListener('load', () => {
        workerMap = JSON.parse(xhr.response);
    });

    xhr.open('GET', 'http://localhost:5555/get_org_ledger/' + id,false);
    xhr.send(JSON.stringify(postParameters));
    return workerMap
}

function createData(name, rank, coverages, requests,id) {

  return {name,rank,coverages, requests,id};
}

let rows;
function createAllData(props) {
    empty = false;
    rows = [];
    const workerMap = getData(props.orgID);
    if (workerMap == null) {
        empty = true;
    }
    for (let worker in workerMap) {
        rows.push(createData(workerMap[worker].name,getHelpfulnessIcon(workerMap[worker].rank), workerMap[worker].coverages,
         workerMap[worker].requests, workerMap[worker].id));
    }
}

const useStyles = makeStyles({
  root: {
    width: '100%',
  },
  container: {

    maxHeight: 440,
  },
  tooltip: {
    fontSize: 300,
  }

});

export default function Leaderboard(handlers) {
  const info = "Earn a higher score by covering others with high helpfulness 🔥versus low helpfulness ❄️!";
  createAllData(handlers)
  const classes = useStyles();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };
   if (empty) {
    return <Paper className={classes.root}> </Paper>;
   }
  return (
    <Paper elevation={4} className={classes.root}>
    <br/>
       <h3 className={"Group"} >Group ID: {handlers.orgID}</h3>

         <h1 style={classes.title}> {handlers.orgName} </h1>
         <h3 className={"Description"} >{ "\""+ handlers.orgDescription + "\""}</h3>
      <TableContainer className={classes.container}>
        <Table className={classes.table} stickyHeader aria-label="leaderboard">
          <TableHead>
            <TableRow>
              {columns.map((column) => (

                column.id === "rank" ?
                <Tooltip style={classes.tooltip} className={"tooltip-helpfulness"}placement={"bottom"}
                 title={info} aria-label="add">
                 <TableCell
                    key={column.id}
                    align={column.align}
                    style={{ minWidth: column.minWidth, fontFamily: 'sans-serif'}}
                 >
                 {column.label}
                 </TableCell>
                 </Tooltip>
                :
                <TableCell
                  key={column.id}
                  align={column.align}
                  style={{ minWidth: column.minWidth, fontFamily: 'sans-serif'}}
                >
                  {column.label}
                </TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row) => {
              return (
                <TableRow hover role="checkbox" tabIndex={-1} key={row.code}>
                  {columns.map((column) => {
                    const value = row[column.id];
                    if (column.id === "id"){
                        return;
                    }
                    if (column.id === "name") {
                       return (
                        <TableCell style={classes.tableCell} key={column.id} align={column.align}>
                           <h3
                           onClick={() => {
                            handlers.history.push({
                                pathname: "/user_profile",
                                state: {
                                    userId: row["id"],
                                    firstGetInfo : true,
                                    firstGetFacts : true,
                                }})}}>
                            <span className = {"leaderboard-name"}>{value}</span> <span className =
                            'owner-tag'>{(row["id"] === handlers.orgOwner ? '(Owner)' : '')}</span></h3>
                        </TableCell>
                       );
                    }else {
                      return (
                        <TableCell key={column.id} align={column.align}>
                            {column.format && typeof value === 'number' ? column.format(value) : value}
                        </TableCell>
                       );
                    }

                  })}
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  );
}
