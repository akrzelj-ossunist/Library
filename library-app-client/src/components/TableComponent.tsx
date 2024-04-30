import {
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import OptionsIcon from "../assets/OptionsIcon";
import { useRef, useState } from "react";
import { useOnClickOutside } from "usehooks-ts";
import { Link, useLocation } from "react-router-dom";

const TableComponent: React.FC<any> = ({ data, columns, ...props }) => {
  const pathname = useLocation().pathname;
  const [optionsMenu, setOptionsMenu] = useState("");
  const optionsRef = useRef<HTMLDivElement | null>(null);
  const { getHeaderGroups, getRowModel } = useReactTable({
    data: data,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  useOnClickOutside(optionsRef, () => setOptionsMenu(""));

  return (
    <>
      <table className="w-[80%] shadow-lg mt-10 tablet:w-[300px] table-auto">
        <thead className="bg-gray-200 font-bold text-gray-500 text-md tablet:hidden">
          {getHeaderGroups().map((headerGroup) => (
            <tr key={headerGroup.id} className="border-[1px] border-gray-300">
              {headerGroup.headers.map((header) => (
                <th key={header.id} className="p-5 w-[20%] text-center">
                  <div>
                    {flexRender(
                      header.column.columnDef.header,
                      header.getContext()
                    )}
                  </div>
                </th>
              ))}
              <th className="p-5 w-[20%] text-center">Options</th>
            </tr>
          ))}
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr className="flex justify-center items-center font-bold w-full">
              <td className="text-center py-4">No data found</td>
            </tr>
          ) : (
            getRowModel().rows.map((row: any) => (
              <tr
                key={row.id}
                className="border-[1px] border-gray-300 tablet:flex tablet:flex-col tablet:items-center  tablet:pl-2 tablet:pr-6 tablet:pb-6 tablet:pt-2 tablet:mb-4">
                {row.getVisibleCells().map((cell: any) => {
                  return cell.getContext().column.id === "isAvailable" ? (
                    <td
                      key={cell.id}
                      className="w-[20%] p-5 font-medium text-md tablet:w-full text-center tablet:pt-0">
                      <label className="desktop:hidden text-xs text-blue-500 font-semibold mt-2">
                        {cell.id.substring(2).toUpperCase()}
                      </label>
                      {cell.getValue() === false ? "Unavailable" : "Available"}
                    </td>
                  ) : (
                    <td
                      key={cell.id}
                      className={`w-[20%] p-5 font-medium text-md tablet:w-full text-center tablet:pt-0 ${
                        cell.getContext().column.id === "title" && "w-[30%]"
                      }`}>
                      <label className="desktop:hidden text-xs text-blue-500 font-semibold mt-2">
                        {cell.id.substring(2).toUpperCase()}
                      </label>
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </td>
                  );
                })}
                <td className="p-5 w-[20%] text-center desktop:pl-9 relative">
                  <OptionsIcon
                    className="cursor-pointer"
                    onClick={() => {
                      row.id === optionsMenu
                        ? setOptionsMenu("")
                        : setOptionsMenu(row.id);
                    }}
                  />
                  {optionsMenu === row.id && (
                    <div
                      ref={optionsRef}
                      className="absolute rounded-md bg-white w-[150px] left-0 font-semibold shadow-xl cursor-pointer z-10 mt-2 flex flex-col">
                      <p
                        className="p-2 hover:bg-slate-100"
                        onClick={() => {
                          props.setBookId(row.original.id);
                          props.setShowModal(true);
                          setOptionsMenu("");
                        }}>
                        Rent
                      </p>
                      <Link
                        to={`${pathname}/edit/${row.original.id}`}
                        className="p-2 hover:bg-slate-100"
                        onClick={() => setOptionsMenu("")}>
                        Edit
                      </Link>
                      <Link
                        to={`${pathname}/delete/${row.original.id}`}
                        className="p-2 hover:bg-slate-100"
                        onClick={() => setOptionsMenu("")}>
                        Delete
                      </Link>
                    </div>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </>
  );
};

export default TableComponent;
